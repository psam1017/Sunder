/*
     테스트 더미 데이터(단, i는 0부터 시작)
    선생님 : id = teacherId, name = teacher, password = 1234
    학생 1 : id = studentId1, password = password1, name = student1, grade = 1
    학생 2 : id = studentId2, password = password2, name = student2, grade = 2
    교과서 0 : title = title + i, chapter = chapter + i, amount = 10, contentType = WORD
    교과서 1 : title = title + i, chapter = chapter + i, amount = 10, contentType = MAIN_TEXT
    교과서 0 단어 : word + i, meaning + i
    교과서 1 문장 : sentence + i, meaning + i
    학생 0의 교과서 0 성적 : 4개 틀림(0 ~ 3까지)
    학생 0의 교과서 1 성적 : 3개 틀림(0 ~ 2까지)
    학생 1의 교과서 0 성적 : 6개 틀림(0 ~ 5까지)
 */

insert into teachers(teacher_id, teacher_name, password) values ("teacherId", "teacher", "1234");
set @teacher_id = (select id from teachers where teacher_id = "teacherId");

insert into students(student_id, password, student_name, grade, teacher_id)
values   ("studentId1", "password1", "student1", 1, @teacher_id),
         ("studentId2", "password2", "student2", 2, @teacher_id);
set @student1_id = (select id from students where student_id = "studentId1");
set @student2_id = (select id from students where student_id = "studentId2");

insert into books(title, chapter, content_type, amount, teacher_id)
values ("book1", "chapter1", "WORD", 10, @teacher_id),
       ("book2", "chapter1", "MAIN_TEXT", 10, @teacher_id);

set @book1_id = (select id from books where title = "book1");
set @book2_id = (select id from books where title = "book2");

insert into words(word, meaning, book_id)
values   ("word0", "meaning0", @book1_id),
         ("word1", "meaning1", @book1_id),
         ("word2", "meaning2", @book1_id),
         ("word3", "meaning3", @book1_id),
         ("word4", "meaning4", @book1_id),
         ("word5", "meaning5", @book1_id),
         ("word6", "meaning6", @book1_id),
         ("word7", "meaning7", @book1_id),
         ("word8", "meaning8", @book1_id),
         ("word9", "meaning9", @book1_id);

insert into sentences(sentence, meaning, book_id)
values   ("sentence0", "meaning0", @book2_id),
         ("sentence1", "meaning1", @book2_id),
         ("sentence2", "meaning2", @book2_id),
         ("sentence3", "meaning3", @book2_id),
         ("sentence4", "meaning4", @book2_id),
         ("sentence5", "meaning5", @book2_id),
         ("sentence6", "meaning6", @book2_id),
         ("sentence7", "meaning7", @book2_id),
         ("sentence8", "meaning8", @book2_id),
         ("sentence9", "meaning9", @book2_id);

insert into word_wrong(student_id, word_id)
values   (@student1_id, (select id from words where word = "word0")),
         (@student1_id, (select id from words where word = "word1")),
         (@student1_id, (select id from words where word = "word2")),
         (@student1_id, (select id from words where word = "word3"));

insert into sentence_wrong(student_id, sentence_id)
values   (@student1_id, (select id from sentences where sentence = "sentence0")),
         (@student1_id, (select id from sentences where sentence = "sentence1")),
         (@student1_id, (select id from sentences where sentence = "sentence2"));

insert into word_wrong(student_id, word_id)
values   (@student2_id, (select id from words where word = "word0")),
         (@student2_id, (select id from words where word = "word1")),
         (@student2_id, (select id from words where word = "word2")),
         (@student2_id, (select id from words where word = "word3")),
         (@student2_id, (select id from words where word = "word4")),
         (@student2_id, (select id from words where word = "word5"));

insert into scores(book_id, student_id, wrong_count, submit_date)
values   (@book1_id, @student1_id, 4, now()),
         (@book2_id, @student1_id, 3, now()),
         (@book1_id, @student2_id, 6, now());
