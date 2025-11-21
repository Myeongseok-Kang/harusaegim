package io.github.myeongseokkang.harusaegim.service;

import io.github.myeongseokkang.harusaegim.dto.DiaryCreateRequest;
import io.github.myeongseokkang.harusaegim.dto.DiaryUpdateRequest;
import io.github.myeongseokkang.harusaegim.entity.Diary;
import io.github.myeongseokkang.harusaegim.repository.DiaryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final OpenAIClient openAIClient;

    public DiaryService(DiaryRepository diaryRepository, OpenAIClient openAIClient) {
        this.diaryRepository = diaryRepository;
        this.openAIClient = openAIClient;
    }

    @Transactional
    public Diary create(Long userId, DiaryCreateRequest req) {
        LocalDate date = req.getDate();

        Diary existing = diaryRepository.findByUserIdAndDate(userId, date).orElse(null);
        if (existing != null) {
            return existing;
        }

        int score = scoreFromFeeling(req.getFeeling());
        String content = composeWithAI(
                req.getPlace(),
                req.getActivity(),
                req.getFeeling(),
                date,
                score
        );

        Diary diary = new Diary();
        diary.setUserId(userId);
        diary.setDate(date);
        diary.setContent(content);
        diary.setEmotionScore(score);

        return diaryRepository.save(diary);
    }

    public List<Diary> list(Long userId) {
        return diaryRepository.findByUserIdOrderByDateDesc(userId);
    }

    public Diary get(Long userId, Long id) {
        Diary d = diaryRepository.findById(id).orElseThrow();
        if (!d.getUserId().equals(userId)) {
            throw new IllegalStateException("forbidden");
        }
        return d;
    }

    @Transactional
    public Diary update(Long userId, Long id, DiaryUpdateRequest req) {
        Diary d = get(userId, id);
        d.setContent(req.getContent());
        if (req.getEmotionScore() != null) {
            d.setEmotionScore(req.getEmotionScore());
        }
        return d;
    }

    @Transactional
    public void delete(Long userId, Long id) {
        Diary d = get(userId, id);
        diaryRepository.delete(d);
    }

    private int scoreFromFeeling(String feeling) {
        if (feeling == null || feeling.isBlank()) {
            return 10;
        }

        String f = feeling.toLowerCase();

        // 강한 긍정 단어
        String[] strongPos = {
                "행복", "신남", "신났다", "설렘", "설렜", "후련", "후련했",
                "상쾌", "짜릿", "기분 최고", "최고", "즐거웠", "즐거움",
                "재밌었", "재미있었", "뿌듯", "만족", "감사", "고마웠",
                "홀가분", "편안했", "편안함"
        };

        // 보통 긍정 단어
        String[] midPos = {
                "좋았", "좋았음", "좋았다", "좋은", "괜찮았", "괜찮은",
                "나쁘지 않", "안 괜찮진 않", "안 나쁘", "가벼워짐", "가벼워졌",
                "가벼웠", "정리되는 느낌", "정리된 느낌", "정리된 것 같",
                "안정", "차분", "평온", "위로", "위안"
        };

        // 강한 부정 단어
        String[] strongNeg = {
                "최악", "끔찍", "지옥", "막막했", "막막함", "암담",
                "붕괴", "무너졌", "포기하고 싶", "죽고 싶", "토할 것 같",
                "멘붕", "패닉"
        };

        // 보통 부정 단어
        String[] midNeg = {
                "지침", "지쳤", "피곤", "피로", "짜증", "짜증났", "짜증나",
                "귀찮", "힘들", "힘들었", "힘든", "우울", "우울했",
                "불안", "불안했", "걱정", "걱정됐", "걱정이 많",
                "답답", "불편", "후회", "죄책감", "죄책", "공허",
                "침울", "슬펐", "슬펐다", "슬픔", "무기력", "무기력했"
        };

        int score = 10; // 기본 중립 점수

        for (String s : strongPos) {
            if (f.contains(s)) score += 4;
        }
        for (String s : midPos) {
            if (f.contains(s)) score += 2;
        }
        for (String s : strongNeg) {
            if (f.contains(s)) score -= 4;
        }
        for (String s : midNeg) {
            if (f.contains(s)) score -= 2;
        }

        if (f.contains("😀") || f.contains("😄") || f.contains("😁") || f.contains("🥳")) score += 5;
        if (f.contains("😞") || f.contains("😢") || f.contains("😭") || f.contains("😔")) score -= 5;

        if (score < 0) score = 0;
        if (score > 20) score = 20;
        return score;
    }

    private String composeWithAI(String place,
                                 String activity,
                                 String feeling,
                                 LocalDate date,
                                 int score) {

        String system = """
            너는 한국어로 하루 일기를 써 주는 비서야.

            아주 중요한 규칙:

            1) 너는 이미 '장소, 한 일, 기분, 기분 점수' 네 가지 정보를 모두 전달받았다.
               - "정보가 비어 있다", "입력해 달라" 같은 안내 문구는 절대 쓰지 마.
               - 사용자를 다시 질문하지 말고, 완성된 일기만 써.

            2) 반드시 아래 입력값을 일기 안에 자연스럽게 녹여서 한 번 이상 포함해라.
               - 장소(place)
               - 한 일(activity)
               - 느낀 기분(feeling)

            3) 제목 없이, 4~7문장의 1인칭 일기를 한 단락으로 작성한다.
               - 문장 사이에 빈 줄을 넣지 말고, 필요하면 짧은 문장 여러 개로 나눠라.
               - 문장 끝에는 마침표(.)만 사용하고 물음표(?)는 사용하지 마.

            4) 새로운 사건을 지어내지 말고, 내가 준 장소/활동/기분 안에서만 상상해서 풀어 써라.
               - 과한 비극, 공포, 범죄, 죽음 같은 자극적인 내용은 넣지 말 것.

            5) 마지막 문장 다음 줄에 꼭 다음 형식으로 한 줄을 추가한다:
               "오늘의 기분 지수: %d/20"

            6) 아래와 같은 것들은 절대 쓰지 말 것:
               - "정보를 알려달라"는 안내 문구
               - 입력 양식 목록(- 장소: … 같은 형태)
               - 의미 없는 물음표 연속(예: ??, ???, ?????)
               - 영어 따옴표나 특수문자로만 이루어진 문자열
            """.formatted(score);

        String user = """
            오늘 하루에 대한 입력값은 다음과 같다.

            - 날짜: %s
            - 장소(place): %s
            - 한 일(activity): %s
            - 느낀 기분(feeling): %s
            - 오늘의 기분 점수: %d/20

            위의 장소/활동/기분 문장은 일기 안에 최소 한 번 이상 그대로 포함해라.
            이 값들을 다시 물어보지 말고, 바로 1인칭 일기 형식으로 서술해 줘.
            """.formatted(date, place, activity, feeling, score);

        String text = null;
        try {
            text = openAIClient.respond(system, user);
            System.out.println("GPT 응답 >>> " + text);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (text == null || text.isBlank()) {
            String fallback = "%s, %s에서 %s를(을) 했다. 오늘 느낀 감정은 '%s'였다.\n오늘의 기분 지수: %d/20"
                    .formatted(date, place, activity, feeling, score);
            return normalizeText(fallback);
        }

        text = cleanupWeirdChars(text);
        text = normalizeText(text);
        return text;
    }


    private String cleanupWeirdChars(String text) {
        if (text == null) return "";
        String cleaned = text;

        cleaned = cleaned.replace("?", "");

        cleaned = cleaned.replace("\uFFFD", "");

        cleaned = cleaned.replaceAll("\\s{2,}", " ");
        return cleaned.trim();
    }


    private String normalizeText(String text) {
        if (text == null) return "";
        String normalized = text.replace("\r\n", "\n");
        normalized = normalized.replaceAll("\\s*\\n\\s*", " ");
        normalized = normalized.replaceAll("\\s{2,}", " ");
        return normalized.trim();
    }
}
