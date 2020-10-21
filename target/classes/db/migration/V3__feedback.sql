set search_path = feedbackschema;

INSERT INTO feedback(question_id,feedback_path,feedback_response,created_by,created_date,last_modified_by,last_modified_date)
	values (1,'/alerts','{"type" : "select", "selected" : ["like"]}','ADMIN',now(),'ADMIN',now());
INSERT INTO feedback(question_id,user_id,feedback_path,feedback_response,created_by,created_date,last_modified_by,last_modified_date) 
	values (2,'nithin@gsa.gov','/access','{"type" : "multiselect", "selected" : ["FH"]}','ADMIN',now(),'ADMIN',now());