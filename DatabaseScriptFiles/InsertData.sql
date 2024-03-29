delete from test.m_element;
insert into test.m_element values ('muto','MUTO3','TABLE','YEAH');
insert into test.m_element values ('NS','NameSpace','SYSTEM','TESTdayo');
insert into test.m_element values ('NS.NOM','Nomura','SYSTEM','TESTdayo');
insert into test.m_element values ('NS.NOM.harada','harada','EXCEL','TESTdayoxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx');
insert into test.m_element values ('NS.TABLE12','12th table','TABLE','^-^');
insert into test.m_element values ('PROCEDURE1','SYORI1','PROCEDURE','^o^');
insert into test.m_element values ('PROCEDURE2','SYORI2','WINDOWS_APP','^o^');
insert into test.m_element values ('PROCEDURE3','SYORI3','SQL_FILE','^o^');
insert into test.m_element values ('PROCEDURE4','SYORI4','PROCEDURE','^o^');
insert into test.m_element values ('PROCEDURE5','SYORI5','PROCEDURE','^o^');
insert into test.m_element values ('PROCEDURE6','SYORI6','PROCEDURE','^o^');
insert into test.m_element values ('PROCEDURE7','SYORI7','PROCEDURE','^o^');
insert into test.m_element values ('PROCEDURE8','SYORI8','PROCEDURE','^o^');
insert into test.m_element values ('TABLE1','1st table','EXCEL','^-^');
insert into test.m_element values ('TABLE10','10th table','TABLE','^-^');
insert into test.m_element values ('TABLE11','11th table','TABLE','^-^');
insert into test.m_element values ('TABLE2','2nd table','TABLE','^-^');
insert into test.m_element values ('TABLE3','3rd table','CSV_FILE','^-^');
insert into test.m_element values ('TABLE4','4th table','TABLE','^-^');
insert into test.m_element values ('TABLE5','5th table','TABLE','^-^');
insert into test.m_element values ('TABLE6','6th table','TABLE','^-^');
insert into test.m_element values ('TABLE7','7th table','TABLE','^-^');
insert into test.m_element values ('TABLE8','8th table','TABLE','^-^');
insert into test.m_element values ('TABLE9','9th table','TABLE','^-^');
insert into test.m_element values ('test','test','test','test');
insert into test.m_element values ('group1','group1','WINDOWS_APP','comment of group1');
insert into test.m_element values ('group2','group2','WINDOWS_APP','comment of group2');
insert into test.m_element values ('group1.data1','group1.data1','TABLE','comment of group1.data1');
insert into test.m_element values ('group1.process1','group1.process1','PROCEDURE','comment of group1.process1');
insert into test.m_element values ('group2.data1','group2.data1','TABLE','comment of group2.data1');
insert into test.m_element values ('group2.process1','group2.process1','PROCEDURE','comment of group2.process1');

delete from test.m_elmttype;
insert into test.m_elmttype values ('CSV_FILE','file-csv');
insert into test.m_elmttype values ('EXCEL','excel');
insert into test.m_elmttype values ('PROCEDURE','program_red');
insert into test.m_elmttype values ('SQL_FILE','file-sql');
insert into test.m_elmttype values ('TABLE','database');
insert into test.m_elmttype values ('WINDOWS_APP','program_black');

delete from test.t_depndncy;
insert into test.t_depndncy values ('muto','TABLE2','1','0','0','0','gggg');
insert into test.t_depndncy values ('NS.TABLE12','TABLE10','1','0','1','1','rmk16');
insert into test.t_depndncy values ('PROCEDURE1','TABLE1','1','1','1','1','rmk1');
insert into test.t_depndncy values ('PROCEDURE1','TABLE10','1','1','0','0','fff');
insert into test.t_depndncy values ('PROCEDURE1','TABLE2','1','1','1','0','rmk2');
insert into test.t_depndncy values ('PROCEDURE1','TABLE3','1','1','0','0','rmk3');
insert into test.t_depndncy values ('PROCEDURE1','TABLE4','1','0','0','0','rmk4');
insert into test.t_depndncy values ('PROCEDURE1','TABLE5','0','1','1','1','rmk5');
insert into test.t_depndncy values ('PROCEDURE1','TABLE6','0','1','1','0','rmk6');
insert into test.t_depndncy values ('PROCEDURE1','TABLE7','0','1','1','0','rmk7');
insert into test.t_depndncy values ('PROCEDURE1','TABLE8','0','1','0','0','rmk8');
insert into test.t_depndncy values ('PROCEDURE2','NS.TABLE12','0','1','0','0','rest');
insert into test.t_depndncy values ('PROCEDURE2','TABLE2','0','1','1','1','rmk9');
insert into test.t_depndncy values ('PROCEDURE2','TABLE3','0','0','1','0','rmk10');
insert into test.t_depndncy values ('PROCEDURE3','TABLE2','0','0','1','0','dd');
insert into test.t_depndncy values ('PROCEDURE3','TABLE3','0','0','0','1','rmk11');
insert into test.t_depndncy values ('PROCEDURE3','TABLE5','0','0','0','1','ddd');
insert into test.t_depndncy values ('PROCEDURE4','TABLE2','0','1','0','0',NULL);
insert into test.t_depndncy values ('PROCEDURE4','TABLE4','1','0','0','1','rmk12');
insert into test.t_depndncy values ('PROCEDURE5','TABLE5','1','0','1','0','rmk13');
insert into test.t_depndncy values ('PROCEDURE6','TABLE6','0','1','0','0','rmk14');
insert into test.t_depndncy values ('PROCEDURE7','TABLE7','1','1','0','1','rmk15');
insert into test.t_depndncy values ('group1.process1','group2.data1','0','1','0','0','grouping test1');
insert into test.t_depndncy values ('group2.process1','group1.data1','0','1','0','0','grouping test2');