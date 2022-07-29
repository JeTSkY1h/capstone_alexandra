package com.github.JeTSkY1h;

import com.github.JeTSkY1h.book.Book;
import com.github.JeTSkY1h.book.BookRepo;
import com.github.JeTSkY1h.book.BookService;
import nl.siegmann.epublib.util.IOUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.print.DocFlavor;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.fail;

public class BookServiceTest {

    String sep = File.separator;
    BookRepo bookRepo = Mockito.mock(BookRepo.class);

    URL localpack = getClass().getResource("");
    String path = localpack.getPath();
    String bookpath = path.replace("/", File.separator) + ".." + File.separator + ".." + File.separator + ".." + File.separator + "Books";
    BookService bookService = new BookService(bookRepo);

    @Test
    void shouldAddBooks() {
        Book expectedBook = new Book();
        expectedBook.setTitle("War and Peace");
        expectedBook.setAuthor("Tolstoy, graf Leo");
        expectedBook.setGenre(List.of("Historical fiction", "War stories", "Napoleonic Wars, 1800-1815 -- Campaigns -- Russia -- Fiction", "Russia -- History -- Alexander I, 1801-1825 -- Fiction", "Aristocracy (Social class) -- Russia -- Fiction"));
        expectedBook.setDescription("In Russia's struggle with Napoleon, Tolstoy saw a tragedy that involved all mankind. Greater than a historical chronicle, War and Peace is an affirmation of life itself, `a complete picture', as a contemporary reviewer put it, `of everything in which people find their happiness and greatness, their grief and humiliation'. Tolstoy gave his personal approval to this translation, published here in a new single volume edition, which includes an introduction by Henry Gifford, and Tolstoy's important essay `Some Words about War and Peace'.");
        List<Book> books = bookService.refresh();
        Mockito.verify(bookRepo).saveAll(books);
        System.out.println(books);
        Book res = books.get(1);
        res.setCoverPath(null);
        res.setFilePath(null);
        Assertions.assertThat(res).isEqualTo(expectedBook);
    }

    @Test
    void shouldListBooks() {
        bookService.getBooks();
        Mockito.verify(bookRepo).findAll();
    }

    @Test
    void shouldFindBookById() {

        bookService.getById("testId123abc");
        Mockito.verify(bookRepo).findById("testId123abc");
    }

    @Test
   void shouldGetCoverPicByBookId(){
        Book expectedBook = new Book();
        expectedBook.setTitle("War and Peace");
        expectedBook.setAuthor("Tolstoy, graf Leo");
        expectedBook.setFilePath((bookpath + sep + "pg2600.epub").replace("/C:", "C:").replace("/", sep ));
        expectedBook.setGenre(List.of("Historical fiction", "War stories", "Napoleonic Wars, 1800-1815 -- Campaigns -- Russia -- Fiction", "Russia -- History -- Alexander I, 1801-1825 -- Fiction", "Aristocracy (Social class) -- Russia -- Fiction"));
        expectedBook.setCoverPath((bookpath + sep + "WarandPeace.png").replace("/C:", "C:").replace("/", sep ));
        Mockito.when(bookRepo.findById("test123")).thenReturn(Optional.of(expectedBook));

        try {
            InputStream in = new FileInputStream(expectedBook.getCoverPath());
            byte[] expected = IOUtil.toByteArray(in);
             byte[] coverPicByteArr = bookService.getCoverByID("test123");
             Assertions.assertThat(coverPicByteArr).isEqualTo(expected);
        } catch(Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void shouldListChapters(){
        Book expectedBook = new Book();
        expectedBook.setTitle("War and Peace");
        expectedBook.setAuthor("Tolstoy, graf Leo");
        expectedBook.setFilePath((bookpath + sep + "pg2600.epub").replace("/C:", "C:").replace("/", sep ));
        expectedBook.setGenre(List.of("Historical fiction", "War stories", "Napoleonic Wars, 1800-1815 -- Campaigns -- Russia -- Fiction", "Russia -- History -- Alexander I, 1801-1825 -- Fiction", "Aristocracy (Social class) -- Russia -- Fiction"));
        expectedBook.setCoverPath((bookpath + "/WarandPeace.png").replace("/C:", "C:").replace("/", sep ));

        List<String> expectedChapters = List.of("WAR AND PEACE",
                "Contents",
                "BOOK ONE: 1805",
                "CHAPTER I",
                "CHAPTER II",
                "CHAPTER III",
                "CHAPTER IV",
                "CHAPTER V",
                "CHAPTER VI",
                "CHAPTER VII",
                "CHAPTER VIII",
                "CHAPTER IX",
                "CHAPTER X",
                "CHAPTER XI",
                "CHAPTER XII",
                "CHAPTER XIII",
                "CHAPTER XIV",
                "CHAPTER XV",
                "CHAPTER XVI",
                "CHAPTER XVII",
                "CHAPTER XVIII",
                "CHAPTER XIX",
                "CHAPTER XX",
                "CHAPTER XXI",
                "CHAPTER XXII",
                "CHAPTER XXIII",
                "CHAPTER XXIV",
                "CHAPTER XXV",
                "CHAPTER XXVI",
                "CHAPTER XXVII",
                "CHAPTER XXVIII",
                "BOOK TWO: 1805",
                "CHAPTER I",
                "CHAPTER II",
                "CHAPTER III",
                "CHAPTER IV",
                "CHAPTER V",
                "CHAPTER VI",
                "CHAPTER VII",
                "CHAPTER VIII",
                "CHAPTER IX",
                "CHAPTER X",
                "CHAPTER XI",
                "CHAPTER XII",
                "CHAPTER XIII",
                "CHAPTER XIV",
                "CHAPTER XV",
                "CHAPTER XVI",
                "CHAPTER XVII",
                "CHAPTER XVIII",
                "CHAPTER XIX",
                "CHAPTER XX",
                "CHAPTER XXI",
                "BOOK THREE: 1805",
                "CHAPTER I",
                "CHAPTER II",
                "CHAPTER III",
                "CHAPTER IV",
                "CHAPTER V",
                "CHAPTER VI",
                "CHAPTER VII",
                "CHAPTER VIII",
                "CHAPTER IX",
                "CHAPTER X",
                "CHAPTER XI",
                "CHAPTER XII",
                "CHAPTER XIII",
                "CHAPTER XIV",
                "CHAPTER XV",
                "CHAPTER XVI",
                "CHAPTER XVII",
                "CHAPTER XVIII",
                "CHAPTER XIX",
                "BOOK FOUR: 1806",
                "CHAPTER I",
                "CHAPTER II",
                "CHAPTER III",
                "CHAPTER IV",
                "CHAPTER V",
                "CHAPTER VI",
                "CHAPTER VII",
                "CHAPTER VIII",
                "CHAPTER IX",
                "CHAPTER X",
                "CHAPTER XI",
                "CHAPTER XII",
                "CHAPTER XIII",
                "CHAPTER XIV",
                "CHAPTER XV",
                "CHAPTER XVI",
                "BOOK FIVE: 1806 - 07",
                "CHAPTER I",
                "CHAPTER II",
                "CHAPTER III",
                "CHAPTER IV",
                "CHAPTER V",
                "CHAPTER VI",
                "CHAPTER VII",
                "CHAPTER VIII",
                "CHAPTER IX",
                "CHAPTER X",
                "CHAPTER XI",
                "CHAPTER XII",
                "CHAPTER XIII",
                "CHAPTER XIV",
                "CHAPTER XV",
                "CHAPTER XVI",
                "CHAPTER XVII",
                "CHAPTER XVIII",
                "CHAPTER XIX",
                "CHAPTER XX",
                "CHAPTER XXI",
                "CHAPTER XXII",
                "BOOK SIX: 1808 - 10",
                "CHAPTER I",
                "CHAPTER II",
                "CHAPTER III",
                "CHAPTER IV",
                "CHAPTER V",
                "CHAPTER VI",
                "CHAPTER VII",
                "CHAPTER VIII",
                "CHAPTER IX",
                "CHAPTER X",
                "CHAPTER XI",
                "CHAPTER XII",
                "CHAPTER XIII",
                "CHAPTER XIV",
                "CHAPTER XV",
                "CHAPTER XVI",
                "CHAPTER XVII",
                "CHAPTER XVIII",
                "CHAPTER XIX",
                "CHAPTER XX",
                "CHAPTER XXI",
                "CHAPTER XXII",
                "CHAPTER XXIII",
                "CHAPTER XXIV",
                "CHAPTER XXV",
                "CHAPTER XXVI",
                "BOOK SEVEN: 1810 - 11",
                "CHAPTER I",
                "CHAPTER II",
                "CHAPTER III",
                "CHAPTER IV",
                "CHAPTER V",
                "CHAPTER VI",
                "CHAPTER VII",
                "CHAPTER VIII",
                "CHAPTER IX",
                "CHAPTER X",
                "CHAPTER XI",
                "CHAPTER XII",
                "CHAPTER XIII",
                "BOOK EIGHT: 1811 - 12",
                "CHAPTER I",
                "CHAPTER II",
                "CHAPTER III",
                "CHAPTER IV",
                "CHAPTER V",
                "CHAPTER VI",
                "CHAPTER VII",
                "CHAPTER VIII",
                "CHAPTER IX",
                "CHAPTER X",
                "CHAPTER XI",
                "CHAPTER XII",
                "CHAPTER XIII",
                "CHAPTER XIV",
                "CHAPTER XV",
                "CHAPTER XVI",
                "CHAPTER XVII",
                "CHAPTER XVIII",
                "CHAPTER XIX",
                "CHAPTER XX",
                "CHAPTER XXI",
                "CHAPTER XXII",
                "BOOK NINE: 1812",
                "CHAPTER I",
                "CHAPTER II",
                "CHAPTER III",
                "CHAPTER IV",
                "CHAPTER V",
                "CHAPTER VI",
                "CHAPTER VII",
                "CHAPTER VIII",
                "CHAPTER IX",
                "CHAPTER X",
                "CHAPTER XI",
                "CHAPTER XII",
                "CHAPTER XIII",
                "CHAPTER XIV",
                "CHAPTER XV",
                "CHAPTER XVI",
                "CHAPTER XVII",
                "CHAPTER XVIII",
                "CHAPTER XIX",
                "CHAPTER XX",
                "CHAPTER XXI",
                "CHAPTER XXII",
                "CHAPTER XXIII",
                "BOOK TEN: 1812",
                "CHAPTER I",
                "CHAPTER II",
                "CHAPTER III",
                "CHAPTER IV",
                "CHAPTER V",
                "CHAPTER VI",
                "CHAPTER VII",
                "CHAPTER VIII",
                "CHAPTER IX",
                "CHAPTER X",
                "CHAPTER XI",
                "CHAPTER XII",
                "CHAPTER XIII",
                "CHAPTER XIV",
                "CHAPTER XV",
                "CHAPTER XVI",
                "CHAPTER XVII",
                "CHAPTER XVIII",
                "CHAPTER XIX",
                "CHAPTER XX",
                "CHAPTER XXI",
                "CHAPTER XXII",
                "CHAPTER XXIII",
                "CHAPTER XXIV",
                "CHAPTER XXV",
                "CHAPTER XXVI",
                "CHAPTER XXVII",
                "CHAPTER XXVIII",
                "CHAPTER XXIX",
                "CHAPTER XXX",
                "CHAPTER XXXI",
                "CHAPTER XXXII",
                "CHAPTER XXXIII",
                "CHAPTER XXXIV",
                "CHAPTER XXXV",
                "CHAPTER XXXVI",
                "CHAPTER XXXVII",
                "CHAPTER XXXVIII",
                "CHAPTER XXXIX",
                "BOOK ELEVEN: 1812",
                "CHAPTER I",
                "CHAPTER II",
                "CHAPTER III",
                "CHAPTER IV",
                "CHAPTER V",
                "CHAPTER VI",
                "CHAPTER VII",
                "CHAPTER VIII",
                "CHAPTER IX",
                "CHAPTER X",
                "CHAPTER XI",
                "CHAPTER XII",
                "CHAPTER XIII",
                "CHAPTER XIV",
                "CHAPTER XV",
                "CHAPTER XVI",
                "CHAPTER XVII",
                "CHAPTER XVIII",
                "CHAPTER XIX",
                "CHAPTER XX",
                "CHAPTER XXI",
                "CHAPTER XXII",
                "CHAPTER XXIII",
                "CHAPTER XXIV",
                "CHAPTER XXV",
                "CHAPTER XXVI",
                "CHAPTER XXVII",
                "CHAPTER XXVIII",
                "CHAPTER XXIX",
                "CHAPTER XXX",
                "CHAPTER XXXI",
                "CHAPTER XXXII",
                "CHAPTER XXXIII",
                "CHAPTER XXXIV",
                "BOOK TWELVE: 1812",
                "CHAPTER I",
                "CHAPTER II",
                "CHAPTER III",
                "CHAPTER IV",
                "CHAPTER V",
                "CHAPTER VI",
                "CHAPTER VII",
                "CHAPTER VIII",
                "CHAPTER IX",
                "CHAPTER X",
                "CHAPTER XI",
                "CHAPTER XII",
                "CHAPTER XIII",
                "CHAPTER XIV",
                "CHAPTER XV",
                "CHAPTER XVI",
                "BOOK THIRTEEN: 1812",
                "CHAPTER I",
                "CHAPTER II",
                "CHAPTER III",
                "CHAPTER IV",
                "CHAPTER V",
                "CHAPTER VI",
                "CHAPTER VII",
                "CHAPTER VIII",
                "CHAPTER IX",
                "CHAPTER X",
                "CHAPTER XI",
                "CHAPTER XII",
                "CHAPTER XIII",
                "CHAPTER XIV",
                "CHAPTER XV",
                "CHAPTER XVI",
                "CHAPTER XVII",
                "CHAPTER XVIII",
                "CHAPTER XIX",
                "BOOK FOURTEEN: 1812",
                "CHAPTER I",
                "CHAPTER II",
                "CHAPTER III",
                "CHAPTER IV",
                "CHAPTER V",
                "CHAPTER VI",
                "CHAPTER VII",
                "CHAPTER VIII",
                "CHAPTER IX",
                "CHAPTER X",
                "CHAPTER XI",
                "CHAPTER XII",
                "CHAPTER XIII",
                "CHAPTER XIV",
                "CHAPTER XV",
                "CHAPTER XVI",
                "CHAPTER XVII",
                "CHAPTER XVIII",
                "CHAPTER XIX",
                "BOOK FIFTEEN: 1812 - 13",
                "CHAPTER I",
                "CHAPTER II",
                "CHAPTER III",
                "CHAPTER IV",
                "CHAPTER V",
                "CHAPTER VI",
                "CHAPTER VII",
                "CHAPTER VIII",
                "CHAPTER IX",
                "CHAPTER X",
                "CHAPTER XI",
                "CHAPTER XII",
                "CHAPTER XIII",
                "CHAPTER XIV",
                "CHAPTER XV",
                "CHAPTER XVI",
                "CHAPTER XVII",
                "CHAPTER XVIII",
                "CHAPTER XIX",
                "CHAPTER XX",
                "FIRST EPILOGUE: 1813 - 20",
                "CHAPTER I",
                "CHAPTER II",
                "CHAPTER III",
                "CHAPTER IV",
                "CHAPTER V",
                "CHAPTER VI",
                "CHAPTER VII",
                "CHAPTER VIII",
                "CHAPTER IX",
                "CHAPTER X",
                "CHAPTER XI",
                "CHAPTER XII",
                "CHAPTER XIII",
                "CHAPTER XIV",
                "CHAPTER XV",
                "CHAPTER XVI",
                "SECOND EPILOGUE",
                "CHAPTER I",
                "CHAPTER II",
                "CHAPTER III",
                "CHAPTER IV",
                "CHAPTER V",
                "CHAPTER VI",
                "CHAPTER VII",
                "CHAPTER VIII",
                "CHAPTER IX",
                "CHAPTER X",
                "CHAPTER XI",
                "CHAPTER XII");


        Mockito.when(bookRepo.findById("test123")).thenReturn(Optional.of(expectedBook));

        try {
            List<String> chapterList = bookService.getChapters("test123");
            Assertions.assertThat(chapterList).isEqualTo(expectedChapters);
        } catch(Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void shouldGetChapterText(){
        Book expectedBook = new Book();
        expectedBook.setTitle("War and Peace");
        expectedBook.setAuthor("Tolstoy, graf Leo");
        expectedBook.setFilePath((bookpath + sep + "pg2600.epub").replace("/C:", "C:").replace("/", sep ));
        expectedBook.setGenre(List.of("Historical fiction", "War stories", "Napoleonic Wars, 1800-1815 -- Campaigns -- Russia -- Fiction", "Russia -- History -- Alexander I, 1801-1825 -- Fiction", "Aristocracy (Social class) -- Russia -- Fiction"));
        expectedBook.setCoverPath((bookpath +  sep + "WarandPeace.png").replace("/C:", "C:").replace("/", sep ));
        Mockito.when(bookRepo.findById("test123")).thenReturn(Optional.of(expectedBook));
        String chapterText = bookService.getChapter("test123", 5);
        Assertions.assertThat(chapterText).isEqualTo("""
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
                <body class="x-ebookmaker"><div class="chapter" id="pgepubid00005">
                <h2><a id="link2HCH0003"/> CHAPTER III</h2>
                <p>Anna Pávlovna’s reception was in full swing. The spindles hummed steadily and ceaselessly on all sides. With the exception of the aunt, beside whom sat only one elderly lady, who with her thin careworn face was rather out of place in this brilliant society, the whole company had settled into three groups. One, chiefly masculine, had formed round the abbé. Another, of young people, was grouped round the beautiful Princess Hélène, Prince Vasíli’s daughter, and the little Princess Bolkónskaya, very pretty and rosy, though rather too plump for her age. The third group was gathered round Mortemart and Anna Pávlovna.</p>
                <p>The vicomte was a nice-looking young man with soft features and polished manners, who evidently considered himself a celebrity but out of politeness modestly placed himself at the disposal of the circle in which he found himself. Anna Pávlovna was obviously serving him up as a treat to her guests. As a clever maître d’hôtel serves up as a specially choice delicacy a piece of meat that no one who had seen it in the kitchen would have cared to eat, so Anna Pávlovna served up to her guests, first the vicomte and then the abbé, as peculiarly choice morsels. The group about Mortemart immediately began discussing the murder of the Duc d’Enghien. The vicomte said that the Duc d’Enghien had perished by his own magnanimity, and that there were particular reasons for Buonaparte’s hatred of him.</p>
                <p>“Ah, yes! Do tell us all about it, Vicomte,” said Anna Pávlovna, with a pleasant feeling that there was something <i>à la Louis XV</i> in the sound of that sentence: <i>“Contez nous çela, Vicomte.”</i></p>
                <p>The vicomte bowed and smiled courteously in token of his willingness to comply. Anna Pávlovna arranged a group round him, inviting everyone to listen to his tale.</p>
                <p>“The vicomte knew the duc personally,” whispered Anna Pávlovna to one of the guests. “The vicomte is a wonderful raconteur,” said she to another. “How evidently he belongs to the best society,” said she to a third; and the vicomte was served up to the company in the choicest and most advantageous style, like a well-garnished joint of roast beef on a hot dish.</p>
                <p>The vicomte wished to begin his story and gave a subtle smile.</p>
                <p>“Come over here, Hélène, dear,” said Anna Pávlovna to the beautiful young princess who was sitting some way off, the center of another group.</p>
                <p>The princess smiled. She rose with the same unchanging smile with which she had first entered the room—the smile of a perfectly beautiful woman. With a slight rustle of her white dress trimmed with moss and ivy, with a gleam of white shoulders, glossy hair, and sparkling diamonds, she passed between the men who made way for her, not looking at any of them but smiling on all, as if graciously allowing each the privilege of admiring her beautiful figure and shapely shoulders, back, and bosom—which in the fashion of those days were very much exposed—and she seemed to bring the glamour of a ballroom with her as she moved toward Anna Pávlovna. Hélène was so lovely that not only did she not show any trace of coquetry, but on the contrary she even appeared shy of her unquestionable and all too victorious beauty. She seemed to wish, but to be unable, to diminish its effect.</p>
                <p>“How lovely!” said everyone who saw her; and the vicomte lifted his shoulders and dropped his eyes as if startled by something extraordinary when she took her seat opposite and beamed upon him also with her unchanging smile.</p>
                <p>“Madame, I doubt my ability before such an audience,” said he, smilingly inclining his head.</p>
                <p>The princess rested her bare round arm on a little table and considered a reply unnecessary. She smilingly waited. All the time the story was being told she sat upright, glancing now at her beautiful round arm, altered in shape by its pressure on the table, now at her still more beautiful bosom, on which she readjusted a diamond necklace. From time to time she smoothed the folds of her dress, and whenever the story produced an effect she glanced at Anna Pávlovna, at once adopted just the expression she saw on the maid of honor’s face, and again relapsed into her radiant smile.</p>
                <p>The little princess had also left the tea table and followed Hélène.</p>
                <p>“Wait a moment, I’ll get my work.... Now then, what are you thinking of?” she went on, turning to Prince Hippolyte. “Fetch me my workbag.”</p>
                <p>There was a general movement as the princess, smiling and talking merrily to everyone at once, sat down and gaily arranged herself in her seat.</p>
                <p>“Now I am all right,” she said, and asking the vicomte to begin, she took up her work.</p>
                <p>Prince Hippolyte, having brought the workbag, joined the circle and moving a chair close to hers seated himself beside her.</p>
                <p><i>Le charmant Hippolyte</i> was surprising by his extraordinary resemblance to his beautiful sister, but yet more by the fact that in spite of this resemblance he was exceedingly ugly. His features were like his sister’s, but while in her case everything was lit up by a joyous, self-satisfied, youthful, and constant smile of animation, and by the wonderful classic beauty of her figure, his face on the contrary was dulled by imbecility and a constant expression of sullen self-confidence, while his body was thin and weak. His eyes, nose, and mouth all seemed puckered into a vacant, wearied grimace, and his arms and legs always fell into unnatural positions.</p>
                <p>“It’s not going to be a ghost story?” said he, sitting down beside the princess and hastily adjusting his lorgnette, as if without this instrument he could not begin to speak.</p>
                <p>“Why no, my dear fellow,” said the astonished narrator, shrugging his shoulders.</p>
                <p>“Because I hate ghost stories,” said Prince Hippolyte in a tone which showed that he only understood the meaning of his words after he had uttered them.</p>
                <p>He spoke with such self-confidence that his hearers could not be sure whether what he said was very witty or very stupid. He was dressed in a dark-green dress coat, knee breeches of the color of <i>cuisse de nymphe effrayée</i>, as he called it, shoes, and silk stockings.</p>
                <p>The vicomte told his tale very neatly. It was an anecdote, then current, to the effect that the Duc d’Enghien had gone secretly to Paris to visit Mademoiselle George; that at her house he came upon Bonaparte, who also enjoyed the famous actress’ favors, and that in his presence Napoleon happened to fall into one of the fainting fits to which he was subject, and was thus at the duc’s mercy. The latter spared him, and this magnanimity Bonaparte subsequently repaid by death.</p>
                <p>The story was very pretty and interesting, especially at the point where the rivals suddenly recognized one another; and the ladies looked agitated.</p>
                <p>“Charming!” said Anna Pávlovna with an inquiring glance at the little princess.</p>
                <p>“Charming!” whispered the little princess, sticking the needle into her work as if to testify that the interest and fascination of the story prevented her from going on with it.</p>
                <p>The vicomte appreciated this silent praise and smiling gratefully prepared to continue, but just then Anna Pávlovna, who had kept a watchful eye on the young man who so alarmed her, noticed that he was talking too loudly and vehemently with the abbé, so she hurried to the rescue. Pierre had managed to start a conversation with the abbé about the balance of power, and the latter, evidently interested by the young man’s simple-minded eagerness, was explaining his pet theory. Both were talking and listening too eagerly and too naturally, which was why Anna Pávlovna disapproved.</p>
                <p>“The means are ... the balance of power in Europe and the rights of the people,” the abbé was saying. “It is only necessary for one powerful nation like Russia—barbaric as she is said to be—to place herself disinterestedly at the head of an alliance having for its object the maintenance of the balance of power of Europe, and it would save the world!”</p>
                <p>“But how are you to get that balance?” Pierre was beginning.</p>
                <p>At that moment Anna Pávlovna came up and, looking severely at Pierre, asked the Italian how he stood Russian climate. The Italian’s face instantly changed and assumed an offensively affected, sugary expression, evidently habitual to him when conversing with women.</p>
                <p>“I am so enchanted by the brilliancy of the wit and culture of the society, more especially of the feminine society, in which I have had the honor of being received, that I have not yet had time to think of the climate,” said he.</p>
                <p>Not letting the abbé and Pierre escape, Anna Pávlovna, the more conveniently to keep them under observation, brought them into the larger circle.</p>
                </div>
                </body></html>
                """);
    }

    @Test
    void shouldRateBook(){
        Book expectedBook = new Book();
        expectedBook.setTitle("War and Peace");
        expectedBook.setAuthor("Tolstoy, graf Leo");
        expectedBook.setFilePath((bookpath + sep + "pg2600.epub").replace("/C:", "C:").replace("/", sep ));
        expectedBook.setGenre(List.of("Historical fiction", "War stories", "Napoleonic Wars, 1800-1815 -- Campaigns -- Russia -- Fiction", "Russia -- History -- Alexander I, 1801-1825 -- Fiction", "Aristocracy (Social class) -- Russia -- Fiction"));
        expectedBook.setCoverPath((bookpath +  sep + "WarandPeace.png").replace("/C:", "C:").replace("/", sep ));
        expectedBook.setRating(1);
        expectedBook.setRated(1);
        Book ratedExpBook = new Book();
        ratedExpBook.setTitle("War and Peace");
        ratedExpBook.setAuthor("Tolstoy, graf Leo");
        ratedExpBook.setFilePath((bookpath + sep + "pg2600.epub").replace("/C:", "C:").replace("/", sep ));
        ratedExpBook.setGenre(List.of("Historical fiction", "War stories", "Napoleonic Wars, 1800-1815 -- Campaigns -- Russia -- Fiction", "Russia -- History -- Alexander I, 1801-1825 -- Fiction", "Aristocracy (Social class) -- Russia -- Fiction"));
        ratedExpBook.setCoverPath((bookpath +  sep + "WarandPeace.png").replace("/C:", "C:").replace("/", sep ));
        ratedExpBook.setRated(2);
        ratedExpBook.setRating(3);
        Mockito.when(bookRepo.findById("test123")).thenReturn(Optional.of(expectedBook));
        Book actual = bookService.rateBook("test123", 5);
        System.out.println(actual);
        Assertions.assertThat(actual).isEqualTo(ratedExpBook);


    }
}
