CREATE TABLE `m_object` (
  `OBJTID` varchar(20) NOT NULL,
  `OBJTNM` varchar(40) DEFAULT NULL,
  `OBJTTP` varchar(20) DEFAULT NULL,
  `REMARK` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`OBJTID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `m_objttype` (
  `OBJTTP` varchar(20) NOT NULL,
  `SVGFLE` varchar(60) NOT NULL,
  PRIMARY KEY (`OBJTTP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_depndncy` (
  `OBJTID` varchar(20) NOT NULL,
  `DPDOID` varchar(20) NOT NULL,
  `DPDTPC` varchar(1) NOT NULL,
  `DPDTPR` varchar(1) NOT NULL,
  `DPDTPU` varchar(1) NOT NULL,
  `DPDTPD` varchar(1) NOT NULL,
  `REMARK` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`OBJTID`,`DPDOID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;