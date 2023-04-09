package net.ink.core.question.repository;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import net.ink.core.annotation.InkDataTest;
import net.ink.core.question.entity.Question;
import net.ink.core.question.entity.WordHint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@InkDataTest
@DatabaseSetup({
        "classpath:dbunit/entity/member.xml",
        "classpath:dbunit/entity/question.xml",
        "classpath:dbunit/entity/word_hint.xml"
})
public class WordHintRepositoryTest {

    private static final Long QUESTION_ID = 1L;

    @Autowired
    WordHintRepository wordHintRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Test
    public void 질문의_단어_힌트_조회(){
        Question question = questionRepository.findById(QUESTION_ID).orElseThrow();

        List<WordHint> wordHintList = wordHintRepository.findAllByQuestion(question);

        assertEquals(2, wordHintList.size());

        assertEquals("blue", wordHintList.get(0).getWord());
        assertEquals("파란색", wordHintList.get(0).getMeaning());

        assertEquals("coral", wordHintList.get(1).getWord());
        assertEquals("코랄색", wordHintList.get(1).getMeaning());
    }
}
