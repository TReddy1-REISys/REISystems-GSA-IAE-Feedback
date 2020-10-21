set search_path = feedbackschema;

UPDATE question SET status='inactive';

insert into feedbackschema.question(question_desc,question_options,created_by,created_date,last_modified_by,last_modified_date, status)
values(
    'We welcome you to provide feedback on this site. If you have a technical issue or need a question answered, please go to the Federal Service Desk. Thank you.',
    '{"type" : "textarea" , "options" : []}','ADMIN',now(), 'ADMIN',now(), 'active'
    );