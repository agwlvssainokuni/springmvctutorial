--
-- Copyright 2014 agwlvssainokuni
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

CREATE TABLE todo (
	id			INTEGER		NOT NULL	AUTO_INCREMENT,
	posted_by	VARCHAR(64)	NOT NULL,
	posted_at	TIMESTAMP	NOT NULL,
	done_at		TIMESTAMP,
	done_flg	INTEGER		NOT NULL	DEFAULT 0,
	description	VARCHAR(5000)	NOT NULL,
	updated_at	TIMESTAMP	NOT NULL	DEFAULT	CURRENT_TIMESTAMP, 
	created_at	TIMESTAMP	NOT NULL	DEFAULT	CURRENT_TIMESTAMP, 
	lock_version INTEGER	NOT NULL	DEFAULT	1, 
	deleted_flg	INTEGER		NOT NULL	DEFAULT	0, 
	CONSTRAINT todo_pkc PRIMARY KEY (id)
)
;
