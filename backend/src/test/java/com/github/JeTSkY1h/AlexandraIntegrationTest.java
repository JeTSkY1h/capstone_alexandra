package com.github.JeTSkY1h;

import com.github.JeTSkY1h.book.Book;
import com.github.JeTSkY1h.user.*;
import nl.siegmann.epublib.util.IOUtil;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlexandraIntegrationTest {
    @Autowired
    TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    MyUserService myUserService;

    @Test
    void AlexandraIntegrationtest(){



        var newUser = new LoginData("testUser", "testPassword");
        //create User
        ResponseEntity<LoginResponse> createUserResponse = restTemplate.postForEntity("/api/user", newUser, LoginResponse.class);
        Assertions.assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        //failed Login
        ResponseEntity<LoginResponse> failedLoginResponse = restTemplate.postForEntity("/api/auth", new LoginData("wronguser", "wrongpassword"), LoginResponse.class);
        Assertions.assertThat(failedLoginResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        //login
        ResponseEntity<LoginResponse> loginResponseNoAdmin = restTemplate.postForEntity("/api/auth", new LoginData("testUser", "testPassword"), LoginResponse.class);
        Assertions.assertThat(loginResponseNoAdmin.getStatusCode()).isEqualTo(HttpStatus.OK);
        String jwtNoAdmin = loginResponseNoAdmin.getBody().getToken();

        Assertions.assertThat(jwtNoAdmin).isNotBlank();

        //try to refresh with no admin User
        ResponseEntity<Void> refreshTry = restTemplate.exchange("/api/books/refresh",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(jwtNoAdmin)),
                Void.class
        );
        Assertions.assertThat(refreshTry.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        //give user Admin Rights
        ResponseEntity<String> userId = restTemplate.exchange("/api/user/",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(jwtNoAdmin)),
                String.class
        );
        MyUser testUser = myUserService.findByUsername("testUser").orElseThrow();
        myUserService.giveAdmin(testUser);
        MyUser testUserAdmin = myUserService.findByUsername("testUser").orElseThrow();
        Assertions.assertThat(testUserAdmin.getRoles()).contains("admin");

        //generate new login token
        ResponseEntity<LoginResponse> loginResponseAdmin = restTemplate.postForEntity("/api/auth", new LoginData("testUser", "testPassword"), LoginResponse.class);
        Assertions.assertThat(loginResponseAdmin.getStatusCode()).isEqualTo(HttpStatus.OK);
        String jwtAdmin = loginResponseAdmin.getBody().getToken();


        //Refresh book list
        ResponseEntity<Void> refresh = restTemplate.exchange("/api/books/refresh",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(jwtAdmin)),
                Void.class
        );
        Assertions.assertThat(refresh.getStatusCode()).isEqualTo(HttpStatus.OK);

        //get books
        ResponseEntity<Book[]> books = restTemplate.exchange("/api/books",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(jwtAdmin)),
                Book[].class
        );
        Assertions.assertThat(books.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(books.getBody()[1].getTitle()).isEqualTo("War and Peace");

        //getChapterList
        ResponseEntity<String[]> chapters = restTemplate.exchange("/api/books/" + books.getBody()[1].getId() + "/chapter",
                HttpMethod.GET,
                new HttpEntity<>( createHeaders(jwtAdmin)),
                String[].class
        );
        Assertions.assertThat(chapters.getBody().length).isEqualTo(384);


        //getChapter
        ResponseEntity<String> chapterText = restTemplate.exchange("/api/books/" + books.getBody()[1].getId() + "/chapter/3",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(jwtAdmin)),
                String.class
        );

        Assertions.assertThat( chapterText.getBody())
                .isEqualTo("""
                <?xml version='1.0' encoding='utf-8'?>
                <!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.1//EN' 'http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd'>
                <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
                <head>
                <meta name="generator" content="HTML Tidy for HTML5 for Linux version 5.6.0"/>
                <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8"/>
                <meta http-equiv="Content-Style-Type" content="text/css"/>
                <title>The Project Gutenberg eBook of War and Peace, by Leo Tolstoy</title>
                <link rel="icon" href="3465067894307608174_cover.jpg" type="image/x-cover"/>
                                
                                
                                
                <link href="0.css" rel="stylesheet" type="text/css"/>
                <link href="1.css" rel="stylesheet" type="text/css"/>
                <link href="pgepub.css" rel="stylesheet" type="text/css"/>
                <meta name="generator" content="Ebookmaker 0.11.30 by Project Gutenberg"/>
                </head>
                <body class="x-ebookmaker"><div class="chapter" id="pgepubid00003">
                <h2><a id="link2HCH0001"/> CHAPTER I</h2>
                <p>?Well, Prince, so Genoa and Lucca are now just family estates of the Buonapartes. But I warn you, if you don?t tell me that this means war, if you still try to defend the infamies and horrors perpetrated by that Antichrist?I really believe he is Antichrist?I will have nothing more to do with you and you are no longer my friend, no longer my ?faithful slave,? as you call yourself! But how do you do? I see I have frightened you?sit down and tell me all the news.?</p>
                <p>It was in July, 1805, and the speaker was the well-known Anna Pávlovna Schérer, maid of honor and favorite of the Empress Márya Fëdorovna. With these words she greeted Prince Vasíli Kurágin, a man of high rank and importance, who was the first to arrive at her reception. Anna Pávlovna had had a cough for some days. She was, as she said, suffering from <i>la grippe; grippe</i> being then a new word in St. Petersburg, used only by the elite.</p>
                <p>All her invitations without exception, written in French, and delivered by a scarlet-liveried footman that morning, ran as follows:</p>
                <p>?If you have nothing better to do, Count (or Prince), and if the prospect of spending an evening with a poor invalid is not too terrible, I shall be very charmed to see you tonight between 7 and 10?Annette Schérer.?</p>
                <p>?Heavens! what a virulent attack!? replied the prince, not in the least disconcerted by this reception. He had just entered, wearing an embroidered court uniform, knee breeches, and shoes, and had stars on his breast and a serene expression on his flat face. He spoke in that refined French in which our grandfathers not only spoke but thought, and with the gentle, patronizing intonation natural to a man of importance who had grown old in society and at court. He went up to Anna Pávlovna, kissed her hand, presenting to her his bald, scented, and shining head, and complacently seated himself on the sofa.</p>
                <p>?First of all, dear friend, tell me how you are. Set your friend?s mind at rest,? said he without altering his tone, beneath the politeness and affected sympathy of which indifference and even irony could be discerned.</p>
                <p>?Can one be well while suffering morally? Can one be calm in times like these if one has any feeling?? said Anna Pávlovna. ?You are staying the whole evening, I hope??</p>
                <p>?And the fete at the English ambassador?s? Today is Wednesday. I must put in an appearance there,? said the prince. ?My daughter is coming for me to take me there.?</p>
                <p>?I thought today?s fete had been canceled. I confess all these festivities and fireworks are becoming wearisome.?</p>
                <p>?If they had known that you wished it, the entertainment would have been put off,? said the prince, who, like a wound-up clock, by force of habit said things he did not even wish to be believed.</p>
                <p>?Don?t tease! Well, and what has been decided about Novosíltsev?s dispatch? You know everything.?</p>
                <p>?What can one say about it?? replied the prince in a cold, listless tone. ?What has been decided? They have decided that Buonaparte has burnt his boats, and I believe that we are ready to burn ours.?</p>
                <p>Prince Vasíli always spoke languidly, like an actor repeating a stale part. Anna Pávlovna Schérer on the contrary, despite her forty years, overflowed with animation and impulsiveness. To be an enthusiast had become her social vocation and, sometimes even when she did not feel like it, she became enthusiastic in order not to disappoint the expectations of those who knew her. The subdued smile which, though it did not suit her faded features, always played round her lips expressed, as in a spoiled child, a continual consciousness of her charming defect, which she neither wished, nor could, nor considered it necessary, to correct.</p>
                <p>In the midst of a conversation on political matters Anna Pávlovna burst out:</p>
                <p>?Oh, don?t speak to me of Austria. Perhaps I don?t understand things, but Austria never has wished, and does not wish, for war. She is betraying us! Russia alone must save Europe. Our gracious sovereign recognizes his high vocation and will be true to it. That is the one thing I have faith in! Our good and wonderful sovereign has to perform the noblest role on earth, and he is so virtuous and noble that God will not forsake him. He will fulfill his vocation and crush the hydra of revolution, which has become more terrible than ever in the person of this murderer and villain! We alone must avenge the blood of the just one.... Whom, I ask you, can we rely on?... England with her commercial spirit will not and cannot understand the Emperor Alexander?s loftiness of soul. She has refused to evacuate Malta. She wanted to find, and still seeks, some secret motive in our actions. What answer did Novosíltsev get? None. The English have not understood and cannot understand the self-abnegation of our Emperor who wants nothing for himself, but only desires the good of mankind. And what have they promised? Nothing! And what little they have promised they will not perform! Prussia has always declared that Buonaparte is invincible, and that all Europe is powerless before him.... And I don?t believe a word that Hardenburg says, or Haugwitz either. This famous Prussian neutrality is just a trap. I have faith only in God and the lofty destiny of our adored monarch. He will save Europe!?</p>
                <p>She suddenly paused, smiling at her own impetuosity.</p>
                <p>?I think,? said the prince with a smile, ?that if you had been sent instead of our dear Wintzingerode you would have captured the King of Prussia?s consent by assault. You are so eloquent. Will you give me a cup of tea??</p>
                <p>?In a moment. <i>À propos</i>,? she added, becoming calm again, ?I am expecting two very interesting men tonight, le Vicomte de Mortemart, who is connected with the Montmorencys through the Rohans, one of the best French families. He is one of the genuine <i>émigrés</i>, the good ones. And also the Abbé Morio. Do you know that profound thinker? He has been received by the Emperor. Had you heard??</p>
                <p>?I shall be delighted to meet them,? said the prince. ?But tell me,? he added with studied carelessness as if it had only just occurred to him, though the question he was about to ask was the chief motive of his visit, ?is it true that the Dowager Empress wants Baron Funke to be appointed first secretary at Vienna? The baron by all accounts is a poor creature.?</p>
                <p>Prince Vasíli wished to obtain this post for his son, but others were trying through the Dowager Empress Márya Fëdorovna to secure it for the baron.</p>
                <p>Anna Pávlovna almost closed her eyes to indicate that neither she nor anyone else had a right to criticize what the Empress desired or was pleased with.</p>
                <p>?Baron Funke has been recommended to the Dowager Empress by her sister,? was all she said, in a dry and mournful tone.</p>
                <p>As she named the Empress, Anna Pávlovna?s face suddenly assumed an expression of profound and sincere devotion and respect mingled with sadness, and this occurred every time she mentioned her illustrious patroness. She added that Her Majesty had deigned to show Baron Funke <i>beaucoup d?estime</i>, and again her face clouded over with sadness.</p>
                <p>The prince was silent and looked indifferent. But, with the womanly and courtierlike quickness and tact habitual to her, Anna Pávlovna wished both to rebuke him (for daring to speak as he had done of a man recommended to the Empress) and at the same time to console him, so she said:</p>
                <p>?Now about your family. Do you know that since your daughter came out everyone has been enraptured by her? They say she is amazingly beautiful.?</p>
                <p>The prince bowed to signify his respect and gratitude.</p>
                <p>?I often think,? she continued after a short pause, drawing nearer to the prince and smiling amiably at him as if to show that political and social topics were ended and the time had come for intimate conversation??I often think how unfairly sometimes the joys of life are distributed. Why has fate given you two such splendid children? I don?t speak of Anatole, your youngest. I don?t like him,? she added in a tone admitting of no rejoinder and raising her eyebrows. ?Two such charming children. And really you appreciate them less than anyone, and so you don?t deserve to have them.?</p>
                <p>And she smiled her ecstatic smile.</p>
                <p>?I can?t help it,? said the prince. ?Lavater would have said I lack the bump of paternity.?</p>
                <p>?Don?t joke; I mean to have a serious talk with you. Do you know I am dissatisfied with your younger son? Between ourselves? (and her face assumed its melancholy expression), ?he was mentioned at Her Majesty?s and you were pitied....?</p>
                <p>The prince answered nothing, but she looked at him significantly, awaiting a reply. He frowned.</p>
                <p>?What would you have me do?? he said at last. ?You know I did all a father could for their education, and they have both turned out fools. Hippolyte is at least a quiet fool, but Anatole is an active one. That is the only difference between them.? He said this smiling in a way more natural and animated than usual, so that the wrinkles round his mouth very clearly revealed something unexpectedly coarse and unpleasant.</p>
                <p>?And why are children born to such men as you? If you were not a father there would be nothing I could reproach you with,? said Anna Pávlovna, looking up pensively.</p>
                <p>?I am your faithful slave and to you alone I can confess that my children are the bane of my life. It is the cross I have to bear. That is how I explain it to myself. It can?t be helped!?</p>
                <p>He said no more, but expressed his resignation to cruel fate by a gesture. Anna Pávlovna meditated.</p>
                <p>?Have you never thought of marrying your prodigal son Anatole?? she asked. ?They say old maids have a mania for matchmaking, and though I don?t feel that weakness in myself as yet, I know a little person who is very unhappy with her father. She is a relation of yours, Princess Mary Bolkónskaya.?</p>
                <p>Prince Vasíli did not reply, though, with the quickness of memory and perception befitting a man of the world, he indicated by a movement of the head that he was considering this information.</p>
                <p>?Do you know,? he said at last, evidently unable to check the sad current of his thoughts, ?that Anatole is costing me forty thousand rubles a year? And,? he went on after a pause, ?what will it be in five years, if he goes on like this?? Presently he added: ?That?s what we fathers have to put up with.... Is this princess of yours rich??</p>
                <p>?Her father is very rich and stingy. He lives in the country. He is the well-known Prince Bolkónski who had to retire from the army under the late Emperor, and was nicknamed ?the King of Prussia.? He is very clever but eccentric, and a bore. The poor girl is very unhappy. She has a brother; I think you know him, he married Lise Meinen lately. He is an aide-de-camp of Kutúzov?s and will be here tonight.?</p>
                <p>?Listen, dear Annette,? said the prince, suddenly taking Anna Pávlovna?s hand and for some reason drawing it downwards. ?Arrange that affair for me and I shall always be your most devoted slave-<i>slafe</i> with an <i>f</i>, as a village elder of mine writes in his reports. She is rich and of good family and that?s all I want.?</p>
                <p>And with the familiarity and easy grace peculiar to him, he raised the maid of honor?s hand to his lips, kissed it, and swung it to and fro as he lay back in his armchair, looking in another direction.</p>
                <p><i>?Attendez,?</i> said Anna Pávlovna, reflecting, ?I?ll speak to Lise, young Bolkónski?s wife, this very evening, and perhaps the thing can be arranged. It shall be on your family?s behalf that I?ll start my apprenticeship as old maid.?</p>
                </div>
                </body></html>
                """);

        //getCover image
        ResponseEntity<byte[]> coverPic = restTemplate.exchange("/api/books/cover/" + books.getBody()[1].getId(),
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(jwtAdmin)),
                byte[].class
        );
        byte[] expected;
        try(InputStream in = new BufferedInputStream(new FileInputStream(books.getBody()[1].getCoverPath()))) {
            expected = IOUtil.toByteArray(in);
            Assertions.assertThat(coverPic.getBody()).isEqualTo(expected);
        } catch(Exception e) {
            e.printStackTrace();
            fail();
        }

        //set UserBookData
        BookUserData bookUserData = new BookUserData("test123", 200, 200, 200, 3, 34);
        ResponseEntity<BookUserData[]> putResp = restTemplate.exchange("/api/user/bookdata",
                HttpMethod.PUT,
                new HttpEntity<>(bookUserData, createHeaders(jwtAdmin)),
                BookUserData[].class
        );
        Assertions.assertThat(putResp.getStatusCode()).isEqualTo(HttpStatus.OK);


        //get UserBookdata
        ResponseEntity<BookUserData[]> bookUserDataResp = restTemplate.exchange("/api/user/bookdata",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(jwtAdmin)),
                BookUserData[].class
        );

        Assertions.assertThat(bookUserDataResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(bookUserDataResp.getBody()[0]).isEqualTo(bookUserData);

    }




    final HttpHeaders createHeaders(String jwt) {
        String authHeaderValue = "Bearer " + jwt;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeaderValue);
        return headers;
    }
}
