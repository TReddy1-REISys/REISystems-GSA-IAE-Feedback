set search_path = feedbackschema;

ALTER TABLE question ADD COLUMN status varchar(10);

UPDATE question SET status='inactive';

insert into feedbackschema.question(question_desc,question_options,created_by,created_date,last_modified_by,last_modified_date, status)
values(
    'What is or is not working well for you on our website? Include your contact information if you would like us to follow up with you.',
    '{"type" : "textarea" , "options" : []}','ADMIN',now(), 'ADMIN',now(), 'active'
    );