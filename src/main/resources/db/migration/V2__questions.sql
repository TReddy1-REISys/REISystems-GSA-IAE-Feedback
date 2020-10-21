set search_path = feedbackschema;

insert into question(question_desc,question_options,created_by,created_date,last_modified_by,last_modified_date)
	values ('What do you like or dislike about beta.SAM.gov?', '{"type" : "textarea" , "options" : []}','ADMIN',now(), 'ADMIN',now());
insert into question(question_desc,question_options,created_by,created_date,last_modified_by,last_modified_date)
	values ('What changes or improvements would you suggest?', '{"type" : "textarea" , "options" : []}','ADMIN',now(), 'ADMIN',now());
insert into question(question_desc,question_options,created_by,created_date,last_modified_by,last_modified_date)
	values ('May we contact you if we have questions about your feedback?', '{"type" : "radio-text" , "options" : ["yes","no"]}','ADMIN',now(), 'ADMIN',now()); 
