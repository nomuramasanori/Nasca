CREATE TABLE `m_element` (
  `elmtid` varchar(20) NOT NULL,
  `elmtnm` varchar(40) DEFAULT NULL,
  `elmttp` varchar(20) DEFAULT NULL,
  `remark` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`elmtID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `m_elmttype` (
  `elmttp` varchar(20) NOT NULL,
  `svgfle` varchar(60) NOT NULL,
  PRIMARY KEY (`elmttp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_depndncy` (
  `elmtid` varchar(20) NOT NULL,
  `dpdeid` varchar(20) NOT NULL,
  `dpdtpc` varchar(1) NOT NULL,
  `dpdtpr` varchar(1) NOT NULL,
  `dpdtpu` varchar(1) NOT NULL,
  `dpdtpd` varchar(1) NOT NULL,
  `remark` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`elmtid`,`dpdeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;