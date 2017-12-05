-- Run this on an empty database. You should see an entry inserted into the
-- Activity table for each of these statements.

INSERT INTO ThoughtwareDB.Project (
	name
) VALUES (
	'test project'
);

INSERT INTO ThoughtwareDB.Milestone (
	start_date,
    end_date,
    project_id
) VALUES (
	'1986-01-01',
    '2086-01-01',
    1
);

INSERT INTO ThoughtwareDB.ProjectFile (
	file_location,
    project_id
) VALUES (
    '/home/user',
    1
);

INSERT INTO ThoughtwareDB.User (
	username,
	hashed_password,
    first_name,
    last_name,
    address1,
    city,
    state,
    zipcode,
    security_question,
    security_answer,
    email
) VALUES (
	'test user',
    'nonsense',
    'first',
    'last',
    'address1',
    'city',
    'VA',
    22222,
    0,
    'asdf',
    'ghjikl'
);

INSERT INTO ThoughtwareDB.UserProjectAssociation (
	project_id,
    user_id
) VALUES (
	1,
    1
);

INSERT INTO ThoughtwareDB.Message (
	message_text,
    project_id,
    user_id
) VALUES (
	'hello world',
    1,
    1
);

SELECT * FROM ThoughtwareDB.Activity;
