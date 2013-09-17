#ifdef PEG
struct T_SRC {
	char *fl; int ln;
} T_SRC[NTRANS];

void
tr_2_src(int m, char *file, int ln)
{	T_SRC[m].fl = file;
	T_SRC[m].ln = ln;
}

void
putpeg(int n, int m)
{	printf("%5d	trans %4d ", m, n);
	printf("%s:%d\n",
		T_SRC[n].fl, T_SRC[n].ln);
}
#endif

void
settable(void)
{	Trans *T;
	Trans *settr(int, int, int, int, int, char *, int, int, int);

	trans = (Trans ***) emalloc(4*sizeof(Trans **));

	/* proctype 2: never_0 */

	trans[2] = (Trans **) emalloc(9*sizeof(Trans *));

	T = trans[2][5] = settr(339,0,0,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(339,0,1,0,0,"IF", 0, 2, 0);
	    T->nxt	= settr(339,0,3,0,0,"IF", 0, 2, 0);
	trans[2][1]	= settr(335,0,7,3,0,"(((App_event==onComplete)&&(App_event==onmousedown)))", 1, 2, 0);
	trans[2][2]	= settr(336,0,7,1,0,"goto accept_all", 0, 2, 0);
	trans[2][6]	= settr(340,0,7,1,0,".(goto)", 0, 2, 0);
	trans[2][3]	= settr(337,0,5,1,0,"(1)", 0, 2, 0);
	trans[2][4]	= settr(338,0,5,1,0,"goto T0_init", 0, 2, 0);
	trans[2][7]	= settr(341,0,8,1,0,"(1)", 0, 2, 0);
	trans[2][8]	= settr(342,0,0,4,4,"-end-", 0, 3500, 0);

	/* proctype 1: Interaction */

	trans[1] = (Trans **) emalloc(28*sizeof(Trans *));

	trans[1][25]	= settr(332,0,24,1,0,".(goto)", 0, 2, 0);
	T = trans[1][24] = settr(331,0,0,0,0,"DO", 0, 2, 0);
	    T->nxt	= settr(331,0,22,0,0,"DO", 0, 2, 0);
	T = trans[1][22] = settr(329,0,0,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(329,0,1,0,0,"IF", 0, 2, 0);
	    T->nxt	= settr(329,0,3,0,0,"IF", 0, 2, 0);
	trans[1][1]	= settr(308,0,27,5,0,"(flg_exit)", 1, 2, 0);
	trans[1][2]	= settr(309,0,27,1,0,"goto :b9", 0, 2, 0);
	trans[1][23]	= settr(330,0,24,1,0,".(goto)", 0, 2, 0);
	trans[1][3]	= settr(310,0,20,2,0,"else", 0, 2, 0);
	T = trans[1][20] = settr(327,0,0,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(327,0,4,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(327,0,6,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(327,0,8,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(327,0,10,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(327,0,12,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(327,0,14,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(327,0,16,0,0,"IF", 0, 2, 0);
	    T->nxt	= settr(327,0,18,0,0,"IF", 0, 2, 0);
	trans[1][4]	= settr(311,0,5,1,0,"(1)", 0, 2, 0);
	trans[1][5]	= settr(312,0,24,6,6,"App_ch!onload", 1, 3, 0);
	trans[1][21]	= settr(328,0,24,1,0,".(goto)", 0, 2, 0);
	trans[1][6]	= settr(313,0,7,1,0,"(1)", 0, 2, 0);
	trans[1][7]	= settr(314,0,24,7,7,"App_ch!User_Click", 1, 3, 0);
	trans[1][8]	= settr(315,0,9,1,0,"(1)", 0, 2, 0);
	trans[1][9]	= settr(316,0,24,8,8,"App_ch!onfocus", 1, 3, 0);
	trans[1][10]	= settr(317,0,11,1,0,"(1)", 0, 2, 0);
	trans[1][11]	= settr(318,0,24,9,9,"App_ch!onSuccess", 1, 3, 0);
	trans[1][12]	= settr(319,0,13,1,0,"(1)", 0, 2, 0);
	trans[1][13]	= settr(320,0,24,10,10,"App_ch!onmousedown", 1, 3, 0);
	trans[1][14]	= settr(321,0,15,1,0,"(1)", 0, 2, 0);
	trans[1][15]	= settr(322,0,24,11,11,"App_ch!onComplete", 1, 3, 0);
	trans[1][16]	= settr(323,0,17,1,0,"(1)", 0, 2, 0);
	trans[1][17]	= settr(324,0,24,12,12,"App_ch!onmouseup", 1, 3, 0);
	trans[1][18]	= settr(325,0,19,1,0,"(1)", 0, 2, 0);
	trans[1][19]	= settr(326,0,24,13,13,"App_ch!onclick", 1, 3, 0);
	trans[1][26]	= settr(333,0,27,1,0,"break", 0, 2, 0);
	trans[1][27]	= settr(334,0,0,14,14,"-end-", 0, 3500, 0);

	/* proctype 0: App */

	trans[0] = (Trans **) emalloc(309*sizeof(Trans *));

/*->*/	trans[0][4]	= settr(3,32,304,15,15,"D_STEP", 1, 2, 0);
	trans[0][305]	= settr(304,0,304,1,0,".(goto)", 0, 2, 0);
	T = trans[0][304] = settr(303,0,0,0,0,"DO", 0, 2, 0);
	    T->nxt	= settr(303,0,5,0,0,"DO", 0, 2, 0);
	trans[0][5]	= settr(4,0,302,16,16,"App_ch?Int_event", 1, 503, 0);
	T = trans[0][302] = settr(301,0,0,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(301,0,6,0,0,"IF", 0, 2, 0);
	    T->nxt	= settr(301,0,300,0,0,"IF", 0, 2, 0);
	trans[0][6]	= settr(5,0,7,17,0,"((Int_event==onload))", 1, 2, 0);
	trans[0][7]	= settr(6,0,11,18,18,"App_event = onload", 1, 2, 0);
/*->*/	trans[0][11]	= settr(10,32,297,19,19,"D_STEP", 1, 2, 0);
	trans[0][298]	= settr(297,0,297,1,0,".(goto)", 0, 2, 0);
	T = trans[0][297] = settr(296,0,0,0,0,"DO", 0, 2, 0);
	    T->nxt	= settr(296,0,12,0,0,"DO", 0, 2, 0);
	trans[0][12]	= settr(11,0,295,20,20,"App_ch?Int_event", 1, 503, 0);
	T = trans[0][295] = settr(294,0,0,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(294,0,13,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(294,0,272,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(294,0,279,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(294,0,286,0,0,"IF", 0, 2, 0);
	    T->nxt	= settr(294,0,293,0,0,"IF", 0, 2, 0);
	trans[0][13]	= settr(12,0,14,21,0,"((Int_event==onfocus))", 1, 2, 0);
	trans[0][14]	= settr(13,0,18,22,22,"App_event = onfocus", 1, 2, 0);
/*->*/	trans[0][18]	= settr(17,32,269,23,23,"D_STEP", 1, 2, 0);
	trans[0][270]	= settr(269,0,269,1,0,".(goto)", 0, 2, 0);
	T = trans[0][269] = settr(268,0,0,0,0,"DO", 0, 2, 0);
	    T->nxt	= settr(268,0,19,0,0,"DO", 0, 2, 0);
	trans[0][19]	= settr(18,0,267,24,24,"App_ch?Int_event", 1, 503, 0);
	T = trans[0][267] = settr(266,0,0,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(266,0,20,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(266,0,237,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(266,0,244,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(266,0,251,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(266,0,258,0,0,"IF", 0, 2, 0);
	    T->nxt	= settr(266,0,265,0,0,"IF", 0, 2, 0);
	trans[0][20]	= settr(19,0,21,25,0,"((Int_event==onSuccess))", 1, 2, 0);
	trans[0][21]	= settr(20,0,25,26,26,"App_event = onSuccess", 1, 2, 0);
/*->*/	trans[0][25]	= settr(24,32,234,27,27,"D_STEP", 1, 2, 0);
	trans[0][235]	= settr(234,0,234,1,0,".(goto)", 0, 2, 0);
	T = trans[0][234] = settr(233,0,0,0,0,"DO", 0, 2, 0);
	    T->nxt	= settr(233,0,26,0,0,"DO", 0, 2, 0);
	trans[0][26]	= settr(25,0,232,28,28,"App_ch?Int_event", 1, 503, 0);
	T = trans[0][232] = settr(231,0,0,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(231,0,27,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(231,0,34,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(231,0,41,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(231,0,223,0,0,"IF", 0, 2, 0);
	    T->nxt	= settr(231,0,230,0,0,"IF", 0, 2, 0);
	trans[0][27]	= settr(26,0,28,29,0,"((Int_event==onfocus))", 1, 2, 0);
	trans[0][28]	= settr(27,0,32,30,30,"App_event = onfocus", 1, 2, 0);
/*->*/	trans[0][32]	= settr(31,32,269,31,31,"D_STEP", 1, 2, 0);
	trans[0][33]	= settr(32,0,269,1,0,"goto goto_getSeed", 0, 2, 0);
	trans[0][233]	= settr(232,0,234,1,0,".(goto)", 0, 2, 0);
	trans[0][34]	= settr(33,0,35,32,0,"((Int_event==onfocus))", 1, 2, 0);
	trans[0][35]	= settr(34,0,39,33,33,"App_event = onfocus", 1, 2, 0);
/*->*/	trans[0][39]	= settr(38,32,269,34,34,"D_STEP", 1, 2, 0);
	trans[0][40]	= settr(39,0,269,1,0,"goto goto_getSeed", 0, 2, 0);
	trans[0][41]	= settr(40,0,42,35,0,"((Int_event==onmousedown))", 1, 2, 0);
	trans[0][42]	= settr(41,0,46,36,36,"App_event = onmousedown", 1, 2, 0);
/*->*/	trans[0][46]	= settr(45,32,220,37,37,"D_STEP", 1, 2, 0);
	trans[0][221]	= settr(220,0,220,1,0,".(goto)", 0, 2, 0);
	T = trans[0][220] = settr(219,0,0,0,0,"DO", 0, 2, 0);
	    T->nxt	= settr(219,0,47,0,0,"DO", 0, 2, 0);
	trans[0][47]	= settr(46,0,218,38,38,"App_ch?Int_event", 1, 503, 0);
	T = trans[0][218] = settr(217,0,0,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(217,0,48,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(217,0,188,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(217,0,195,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(217,0,202,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(217,0,209,0,0,"IF", 0, 2, 0);
	    T->nxt	= settr(217,0,216,0,0,"IF", 0, 2, 0);
	trans[0][48]	= settr(47,0,49,39,0,"((Int_event==onComplete))", 1, 2, 0);
	trans[0][49]	= settr(48,0,53,40,40,"App_event = onComplete", 1, 2, 0);
/*->*/	trans[0][53]	= settr(52,32,185,41,41,"D_STEP", 1, 2, 0);
	trans[0][186]	= settr(185,0,185,1,0,".(goto)", 0, 2, 0);
	T = trans[0][185] = settr(184,0,0,0,0,"DO", 0, 2, 0);
	    T->nxt	= settr(184,0,54,0,0,"DO", 0, 2, 0);
	trans[0][54]	= settr(53,0,183,42,42,"App_ch?Int_event", 1, 503, 0);
	T = trans[0][183] = settr(182,0,0,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(182,0,55,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(182,0,132,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(182,0,174,0,0,"IF", 0, 2, 0);
	    T->nxt	= settr(182,0,181,0,0,"IF", 0, 2, 0);
	trans[0][55]	= settr(54,0,56,43,0,"((Int_event==User_Click))", 1, 2, 0);
	trans[0][56]	= settr(55,0,60,44,44,"App_event = User_Click", 1, 2, 0);
/*->*/	trans[0][60]	= settr(59,32,129,45,45,"D_STEP", 1, 2, 0);
	trans[0][130]	= settr(129,0,129,1,0,".(goto)", 0, 2, 0);
	T = trans[0][129] = settr(128,0,0,0,0,"DO", 0, 2, 0);
	    T->nxt	= settr(128,0,61,0,0,"DO", 0, 2, 0);
	trans[0][61]	= settr(60,0,127,46,46,"App_ch?Int_event", 1, 503, 0);
	T = trans[0][127] = settr(126,0,0,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(126,0,62,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(126,0,69,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(126,0,76,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(126,0,83,0,0,"IF", 0, 2, 0);
	    T->nxt	= settr(126,0,125,0,0,"IF", 0, 2, 0);
	trans[0][62]	= settr(61,0,63,47,0,"((Int_event==onfocus))", 1, 2, 0);
	trans[0][63]	= settr(62,0,67,48,48,"App_event = onfocus", 1, 2, 0);
/*->*/	trans[0][67]	= settr(66,32,269,49,49,"D_STEP", 1, 2, 0);
	trans[0][68]	= settr(67,0,269,1,0,"goto goto_getSeed", 0, 2, 0);
	trans[0][128]	= settr(127,0,129,1,0,".(goto)", 0, 2, 0);
	trans[0][69]	= settr(68,0,70,50,0,"((Int_event==onfocus))", 1, 2, 0);
	trans[0][70]	= settr(69,0,74,51,51,"App_event = onfocus", 1, 2, 0);
/*->*/	trans[0][74]	= settr(73,32,269,52,52,"D_STEP", 1, 2, 0);
	trans[0][75]	= settr(74,0,269,1,0,"goto goto_getSeed", 0, 2, 0);
	trans[0][76]	= settr(75,0,77,53,0,"((Int_event==onmousedown))", 1, 2, 0);
	trans[0][77]	= settr(76,0,81,54,54,"App_event = onmousedown", 1, 2, 0);
/*->*/	trans[0][81]	= settr(80,32,220,55,55,"D_STEP", 1, 2, 0);
	trans[0][82]	= settr(81,0,220,1,0,"goto goto_validateLogin", 0, 2, 0);
	trans[0][83]	= settr(82,0,84,56,0,"((Int_event==onclick))", 1, 2, 0);
	trans[0][84]	= settr(83,0,88,57,57,"App_event = onclick", 1, 2, 0);
/*->*/	trans[0][88]	= settr(87,32,122,58,58,"D_STEP", 1, 2, 0);
	trans[0][123]	= settr(122,0,122,1,0,".(goto)", 0, 2, 0);
	T = trans[0][122] = settr(121,0,0,0,0,"DO", 0, 2, 0);
	    T->nxt	= settr(121,0,89,0,0,"DO", 0, 2, 0);
	trans[0][89]	= settr(88,0,120,59,59,"App_ch?Int_event", 1, 503, 0);
	T = trans[0][120] = settr(119,0,0,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(119,0,90,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(119,0,97,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(119,0,104,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(119,0,111,0,0,"IF", 0, 2, 0);
	    T->nxt	= settr(119,0,118,0,0,"IF", 0, 2, 0);
	trans[0][90]	= settr(89,0,91,60,0,"((Int_event==onfocus))", 1, 2, 0);
	trans[0][91]	= settr(90,0,95,61,61,"App_event = onfocus", 1, 2, 0);
/*->*/	trans[0][95]	= settr(94,32,269,62,62,"D_STEP", 1, 2, 0);
	trans[0][96]	= settr(95,0,269,1,0,"goto goto_getSeed", 0, 2, 0);
	trans[0][121]	= settr(120,0,122,1,0,".(goto)", 0, 2, 0);
	trans[0][97]	= settr(96,0,98,63,0,"((Int_event==onfocus))", 1, 2, 0);
	trans[0][98]	= settr(97,0,102,64,64,"App_event = onfocus", 1, 2, 0);
/*->*/	trans[0][102]	= settr(101,32,269,65,65,"D_STEP", 1, 2, 0);
	trans[0][103]	= settr(102,0,269,1,0,"goto goto_getSeed", 0, 2, 0);
	trans[0][104]	= settr(103,0,105,66,0,"((Int_event==onmousedown))", 1, 2, 0);
	trans[0][105]	= settr(104,0,109,67,67,"App_event = onmousedown", 1, 2, 0);
/*->*/	trans[0][109]	= settr(108,32,220,68,68,"D_STEP", 1, 2, 0);
	trans[0][110]	= settr(109,0,220,1,0,"goto goto_validateLogin", 0, 2, 0);
	trans[0][111]	= settr(110,0,112,69,0,"((Int_event==onclick))", 1, 2, 0);
	trans[0][112]	= settr(111,0,116,70,70,"App_event = onclick", 1, 2, 0);
/*->*/	trans[0][116]	= settr(115,32,122,71,71,"D_STEP", 1, 2, 0);
	trans[0][117]	= settr(116,0,122,1,0,"goto goto_onGood", 0, 2, 0);
	trans[0][118]	= settr(117,0,119,2,0,"else", 0, 2, 0);
	trans[0][119]	= settr(118,0,122,72,72,"Int_event = __empty__", 1, 2, 0);
	trans[0][124]	= settr(123,0,129,1,0,"break", 0, 2, 0);
	trans[0][125]	= settr(124,0,126,2,0,"else", 0, 2, 0);
	trans[0][126]	= settr(125,0,129,73,73,"Int_event = __empty__", 1, 2, 0);
	trans[0][131]	= settr(130,0,185,1,0,"break", 0, 2, 0);
	trans[0][184]	= settr(183,0,185,1,0,".(goto)", 0, 2, 0);
	trans[0][132]	= settr(131,0,133,74,0,"((Int_event==onmouseup))", 1, 2, 0);
	trans[0][133]	= settr(132,0,137,75,75,"App_event = onmouseup", 1, 2, 0);
/*->*/	trans[0][137]	= settr(136,32,171,76,76,"D_STEP", 1, 2, 0);
	trans[0][172]	= settr(171,0,171,1,0,".(goto)", 0, 2, 0);
	T = trans[0][171] = settr(170,0,0,0,0,"DO", 0, 2, 0);
	    T->nxt	= settr(170,0,138,0,0,"DO", 0, 2, 0);
	trans[0][138]	= settr(137,0,169,77,77,"App_ch?Int_event", 1, 503, 0);
	T = trans[0][169] = settr(168,0,0,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(168,0,139,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(168,0,146,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(168,0,153,0,0,"IF", 0, 2, 0);
	T = T->nxt	= settr(168,0,160,0,0,"IF", 0, 2, 0);
	    T->nxt	= settr(168,0,167,0,0,"IF", 0, 2, 0);
	trans[0][139]	= settr(138,0,140,78,0,"((Int_event==onfocus))", 1, 2, 0);
	trans[0][140]	= settr(139,0,144,79,79,"App_event = onfocus", 1, 2, 0);
/*->*/	trans[0][144]	= settr(143,32,269,80,80,"D_STEP", 1, 2, 0);
	trans[0][145]	= settr(144,0,269,1,0,"goto goto_getSeed", 0, 2, 0);
	trans[0][170]	= settr(169,0,171,1,0,".(goto)", 0, 2, 0);
	trans[0][146]	= settr(145,0,147,81,0,"((Int_event==onfocus))", 1, 2, 0);
	trans[0][147]	= settr(146,0,151,82,82,"App_event = onfocus", 1, 2, 0);
/*->*/	trans[0][151]	= settr(150,32,269,83,83,"D_STEP", 1, 2, 0);
	trans[0][152]	= settr(151,0,269,1,0,"goto goto_getSeed", 0, 2, 0);
	trans[0][153]	= settr(152,0,154,84,0,"((Int_event==onmousedown))", 1, 2, 0);
	trans[0][154]	= settr(153,0,158,85,85,"App_event = onmousedown", 1, 2, 0);
/*->*/	trans[0][158]	= settr(157,32,220,86,86,"D_STEP", 1, 2, 0);
	trans[0][159]	= settr(158,0,220,1,0,"goto goto_validateLogin", 0, 2, 0);
	trans[0][160]	= settr(159,0,161,87,0,"((Int_event==onclick))", 1, 2, 0);
	trans[0][161]	= settr(160,0,165,88,88,"App_event = onclick", 1, 2, 0);
/*->*/	trans[0][165]	= settr(164,32,122,89,89,"D_STEP", 1, 2, 0);
	trans[0][166]	= settr(165,0,122,1,0,"goto goto_onGood", 0, 2, 0);
	trans[0][167]	= settr(166,0,168,2,0,"else", 0, 2, 0);
	trans[0][168]	= settr(167,0,171,90,90,"Int_event = __empty__", 1, 2, 0);
	trans[0][173]	= settr(172,0,185,1,0,"break", 0, 2, 0);
	trans[0][174]	= settr(173,0,175,91,0,"((Int_event==onclick))", 1, 2, 0);
	trans[0][175]	= settr(174,0,179,92,92,"App_event = onclick", 1, 2, 0);
/*->*/	trans[0][179]	= settr(178,32,122,93,93,"D_STEP", 1, 2, 0);
	trans[0][180]	= settr(179,0,122,1,0,"goto goto_onGood", 0, 2, 0);
	trans[0][181]	= settr(180,0,182,2,0,"else", 0, 2, 0);
	trans[0][182]	= settr(181,0,185,94,94,"Int_event = __empty__", 1, 2, 0);
	trans[0][187]	= settr(186,0,220,1,0,"break", 0, 2, 0);
	trans[0][219]	= settr(218,0,220,1,0,".(goto)", 0, 2, 0);
	trans[0][188]	= settr(187,0,189,95,0,"((Int_event==onfocus))", 1, 2, 0);
	trans[0][189]	= settr(188,0,193,96,96,"App_event = onfocus", 1, 2, 0);
/*->*/	trans[0][193]	= settr(192,32,269,97,97,"D_STEP", 1, 2, 0);
	trans[0][194]	= settr(193,0,269,1,0,"goto goto_getSeed", 0, 2, 0);
	trans[0][195]	= settr(194,0,196,98,0,"((Int_event==onfocus))", 1, 2, 0);
	trans[0][196]	= settr(195,0,200,99,99,"App_event = onfocus", 1, 2, 0);
/*->*/	trans[0][200]	= settr(199,32,269,100,100,"D_STEP", 1, 2, 0);
	trans[0][201]	= settr(200,0,269,1,0,"goto goto_getSeed", 0, 2, 0);
	trans[0][202]	= settr(201,0,203,101,0,"((Int_event==onmousedown))", 1, 2, 0);
	trans[0][203]	= settr(202,0,207,102,102,"App_event = onmousedown", 1, 2, 0);
/*->*/	trans[0][207]	= settr(206,32,220,103,103,"D_STEP", 1, 2, 0);
	trans[0][208]	= settr(207,0,220,1,0,"goto goto_validateLogin", 0, 2, 0);
	trans[0][209]	= settr(208,0,210,104,0,"((Int_event==onclick))", 1, 2, 0);
	trans[0][210]	= settr(209,0,214,105,105,"App_event = onclick", 1, 2, 0);
/*->*/	trans[0][214]	= settr(213,32,122,106,106,"D_STEP", 1, 2, 0);
	trans[0][215]	= settr(214,0,122,1,0,"goto goto_onGood", 0, 2, 0);
	trans[0][216]	= settr(215,0,217,2,0,"else", 0, 2, 0);
	trans[0][217]	= settr(216,0,220,107,107,"Int_event = __empty__", 1, 2, 0);
	trans[0][222]	= settr(221,0,234,1,0,"break", 0, 2, 0);
	trans[0][223]	= settr(222,0,224,108,0,"((Int_event==onclick))", 1, 2, 0);
	trans[0][224]	= settr(223,0,228,109,109,"App_event = onclick", 1, 2, 0);
/*->*/	trans[0][228]	= settr(227,32,122,110,110,"D_STEP", 1, 2, 0);
	trans[0][229]	= settr(228,0,122,1,0,"goto goto_onGood", 0, 2, 0);
	trans[0][230]	= settr(229,0,231,2,0,"else", 0, 2, 0);
	trans[0][231]	= settr(230,0,234,111,111,"Int_event = __empty__", 1, 2, 0);
	trans[0][236]	= settr(235,0,269,1,0,"break", 0, 2, 0);
	trans[0][268]	= settr(267,0,269,1,0,".(goto)", 0, 2, 0);
	trans[0][237]	= settr(236,0,238,112,0,"((Int_event==onfocus))", 1, 2, 0);
	trans[0][238]	= settr(237,0,242,113,113,"App_event = onfocus", 1, 2, 0);
/*->*/	trans[0][242]	= settr(241,32,269,114,114,"D_STEP", 1, 2, 0);
	trans[0][243]	= settr(242,0,269,1,0,"goto goto_getSeed", 0, 2, 0);
	trans[0][244]	= settr(243,0,245,115,0,"((Int_event==onfocus))", 1, 2, 0);
	trans[0][245]	= settr(244,0,249,116,116,"App_event = onfocus", 1, 2, 0);
/*->*/	trans[0][249]	= settr(248,32,269,117,117,"D_STEP", 1, 2, 0);
	trans[0][250]	= settr(249,0,269,1,0,"goto goto_getSeed", 0, 2, 0);
	trans[0][251]	= settr(250,0,252,118,0,"((Int_event==onmousedown))", 1, 2, 0);
	trans[0][252]	= settr(251,0,256,119,119,"App_event = onmousedown", 1, 2, 0);
/*->*/	trans[0][256]	= settr(255,32,220,120,120,"D_STEP", 1, 2, 0);
	trans[0][257]	= settr(256,0,220,1,0,"goto goto_validateLogin", 0, 2, 0);
	trans[0][258]	= settr(257,0,259,121,0,"((Int_event==onclick))", 1, 2, 0);
	trans[0][259]	= settr(258,0,263,122,122,"App_event = onclick", 1, 2, 0);
/*->*/	trans[0][263]	= settr(262,32,122,123,123,"D_STEP", 1, 2, 0);
	trans[0][264]	= settr(263,0,122,1,0,"goto goto_onGood", 0, 2, 0);
	trans[0][265]	= settr(264,0,266,2,0,"else", 0, 2, 0);
	trans[0][266]	= settr(265,0,269,124,124,"Int_event = __empty__", 1, 2, 0);
	trans[0][271]	= settr(270,0,297,1,0,"break", 0, 2, 0);
	trans[0][296]	= settr(295,0,297,1,0,".(goto)", 0, 2, 0);
	trans[0][272]	= settr(271,0,273,125,0,"((Int_event==onfocus))", 1, 2, 0);
	trans[0][273]	= settr(272,0,277,126,126,"App_event = onfocus", 1, 2, 0);
/*->*/	trans[0][277]	= settr(276,32,269,127,127,"D_STEP", 1, 2, 0);
	trans[0][278]	= settr(277,0,269,1,0,"goto goto_getSeed", 0, 2, 0);
	trans[0][279]	= settr(278,0,280,128,0,"((Int_event==onmousedown))", 1, 2, 0);
	trans[0][280]	= settr(279,0,284,129,129,"App_event = onmousedown", 1, 2, 0);
/*->*/	trans[0][284]	= settr(283,32,220,130,130,"D_STEP", 1, 2, 0);
	trans[0][285]	= settr(284,0,220,1,0,"goto goto_validateLogin", 0, 2, 0);
	trans[0][286]	= settr(285,0,287,131,0,"((Int_event==onclick))", 1, 2, 0);
	trans[0][287]	= settr(286,0,291,132,132,"App_event = onclick", 1, 2, 0);
/*->*/	trans[0][291]	= settr(290,32,122,133,133,"D_STEP", 1, 2, 0);
	trans[0][292]	= settr(291,0,122,1,0,"goto goto_onGood", 0, 2, 0);
	trans[0][293]	= settr(292,0,294,2,0,"else", 0, 2, 0);
	trans[0][294]	= settr(293,0,297,134,134,"Int_event = __empty__", 1, 2, 0);
	trans[0][299]	= settr(298,0,304,1,0,"break", 0, 2, 0);
	trans[0][303]	= settr(302,0,304,1,0,".(goto)", 0, 2, 0);
	trans[0][300]	= settr(299,0,301,2,0,"else", 0, 2, 0);
	trans[0][301]	= settr(300,0,304,135,135,"Int_event = __empty__", 1, 2, 0);
	trans[0][306]	= settr(305,0,307,1,0,"break", 0, 2, 0);
	trans[0][307]	= settr(306,0,308,136,136,"flg_exit = 1", 1, 2, 0);
	trans[0][308]	= settr(307,0,0,137,137,"-end-", 0, 3500, 0);
	/* np_ demon: */
	trans[_NP_] = (Trans **) emalloc(2*sizeof(Trans *));
	T = trans[_NP_][0] = settr(9997,0,1,_T5,0,"(np_)", 1,2,0);
	    T->nxt	  = settr(9998,0,0,_T2,0,"(1)",   0,2,0);
	T = trans[_NP_][1] = settr(9999,0,1,_T5,0,"(np_)", 1,2,0);
}

Trans *
settr(	int t_id, int a, int b, int c, int d,
	char *t, int g, int tpe0, int tpe1)
{	Trans *tmp = (Trans *) emalloc(sizeof(Trans));

	tmp->atom  = a&(6|32);	/* only (2|8|32) have meaning */
	if (!g) tmp->atom |= 8;	/* no global references */
	tmp->st    = b;
	tmp->tpe[0] = tpe0;
	tmp->tpe[1] = tpe1;
	tmp->tp    = t;
	tmp->t_id  = t_id;
	tmp->forw  = c;
	tmp->back  = d;
	return tmp;
}

Trans *
cpytr(Trans *a)
{	Trans *tmp = (Trans *) emalloc(sizeof(Trans));

	int i;
	tmp->atom  = a->atom;
	tmp->st    = a->st;
#ifdef HAS_UNLESS
	tmp->e_trans = a->e_trans;
	for (i = 0; i < HAS_UNLESS; i++)
		tmp->escp[i] = a->escp[i];
#endif
	tmp->tpe[0] = a->tpe[0];
	tmp->tpe[1] = a->tpe[1];
	for (i = 0; i < 6; i++)
	{	tmp->qu[i] = a->qu[i];
		tmp->ty[i] = a->ty[i];
	}
	tmp->tp    = (char *) emalloc(strlen(a->tp)+1);
	strcpy(tmp->tp, a->tp);
	tmp->t_id  = a->t_id;
	tmp->forw  = a->forw;
	tmp->back  = a->back;
	return tmp;
}

#ifndef NOREDUCE
int
srinc_set(int n)
{	if (n <= 2) return LOCAL;
	if (n <= 2+  DELTA) return Q_FULL_F; /* 's' or nfull  */
	if (n <= 2+2*DELTA) return Q_EMPT_F; /* 'r' or nempty */
	if (n <= 2+3*DELTA) return Q_EMPT_T; /* empty */
	if (n <= 2+4*DELTA) return Q_FULL_T; /* full  */
	if (n ==   5*DELTA) return GLOBAL;
	if (n ==   6*DELTA) return TIMEOUT_F;
	if (n ==   7*DELTA) return ALPHA_F;
	Uerror("cannot happen srinc_class");
	return BAD;
}
int
srunc(int n, int m)
{	switch(m) {
	case Q_FULL_F: return n-2;
	case Q_EMPT_F: return n-2-DELTA;
	case Q_EMPT_T: return n-2-2*DELTA;
	case Q_FULL_T: return n-2-3*DELTA;
	case ALPHA_F:
	case TIMEOUT_F: return 257; /* non-zero, and > MAXQ */
	}
	Uerror("cannot happen srunc");
	return 0;
}
#endif
int cnt;
#ifdef HAS_UNLESS
int
isthere(Trans *a, int b)
{	Trans *t;
	for (t = a; t; t = t->nxt)
		if (t->t_id == b)
			return 1;
	return 0;
}
#endif
#ifndef NOREDUCE
int
mark_safety(Trans *t) /* for conditional safety */
{	int g = 0, i, j, k;

	if (!t) return 0;
	if (t->qu[0])
		return (t->qu[1])?2:1;	/* marked */

	for (i = 0; i < 2; i++)
	{	j = srinc_set(t->tpe[i]);
		if (j >= GLOBAL && j != ALPHA_F)
			return -1;
		if (j != LOCAL)
		{	k = srunc(t->tpe[i], j);
			if (g == 0
			||  t->qu[0] != k
			||  t->ty[0] != j)
			{	t->qu[g] = k;
				t->ty[g] = j;
				g++;
	}	}	}
	return g;
}
#endif
void
retrans(int n, int m, int is, short srcln[], uchar reach[], uchar lpstate[])
	/* process n, with m states, is=initial state */
{	Trans *T0, *T1, *T2, *T3;
	Trans *T4, *T5; /* t_reverse or has_unless */
	int i;
#if defined(HAS_UNLESS) || !defined(NOREDUCE)
	int k;
#endif
#ifndef NOREDUCE
	int g, h, j, aa;
#endif
#ifdef HAS_UNLESS
	int p;
#endif
	if (state_tables >= 4)
	{	printf("STEP 1 %s\n", 
			procname[n]);
		for (i = 1; i < m; i++)
		for (T0 = trans[n][i]; T0; T0 = T0->nxt)
			crack(n, i, T0, srcln);
		return;
	}
	do {
		for (i = 1, cnt = 0; i < m; i++)
		{	T2 = trans[n][i];
			T1 = T2?T2->nxt:(Trans *)0;
/* prescan: */		for (T0 = T1; T0; T0 = T0->nxt)
/* choice in choice */	{	if (T0->st && trans[n][T0->st]
				&&  trans[n][T0->st]->nxt)
					break;
			}
#if 0
		if (T0)
		printf("\tstate %d / %d: choice in choice\n",
		i, T0->st);
#endif
			if (T0)
			for (T0 = T1; T0; T0 = T0->nxt)
			{	T3 = trans[n][T0->st];
				if (!T3->nxt)
				{	T2->nxt = cpytr(T0);
					T2 = T2->nxt;
					imed(T2, T0->st, n, i);
					continue;
				}
				do {	T3 = T3->nxt;
					T2->nxt = cpytr(T3);
					T2 = T2->nxt;
					imed(T2, T0->st, n, i);
				} while (T3->nxt);
				cnt++;
			}
		}
	} while (cnt);
	if (state_tables >= 3)
	{	printf("STEP 2 %s\n", 
			procname[n]);
		for (i = 1; i < m; i++)
		for (T0 = trans[n][i]; T0; T0 = T0->nxt)
			crack(n, i, T0, srcln);
		return;
	}
	for (i = 1; i < m; i++)
	{	if (trans[n][i] && trans[n][i]->nxt) /* optimize */
		{	T1 = trans[n][i]->nxt;
#if 0
			printf("\t\tpull %d (%d) to %d\n",
			T1->st, T1->forw, i);
#endif
			srcln[i] = srcln[T1->st];	/* Oyvind Teig, 5.2.0 */

			if (!trans[n][T1->st]) continue;
			T0 = cpytr(trans[n][T1->st]);
			trans[n][i] = T0;
			reach[T1->st] = 1;
			imed(T0, T1->st, n, i);
			for (T1 = T1->nxt; T1; T1 = T1->nxt)
			{
#if 0
			printf("\t\tpull %d (%d) to %d\n",
				T1->st, T1->forw, i);
#endif
		/*		srcln[i] = srcln[T1->st];  gh: not useful */
				if (!trans[n][T1->st]) continue;
				T0->nxt = cpytr(trans[n][T1->st]);
				T0 = T0->nxt;
				reach[T1->st] = 1;
				imed(T0, T1->st, n, i);
	}	}	}
	if (state_tables >= 2)
	{	printf("STEP 3 %s\n", 
			procname[n]);
		for (i = 1; i < m; i++)
		for (T0 = trans[n][i]; T0; T0 = T0->nxt)
			crack(n, i, T0, srcln);
		return;
	}
#ifdef HAS_UNLESS
	for (i = 1; i < m; i++)
	{	if (!trans[n][i]) continue;
		/* check for each state i if an
		 * escape to some state p is defined
		 * if so, copy and mark p's transitions
		 * and prepend them to the transition-
		 * list of state i
		 */
	 if (!like_java) /* the default */
	 {	for (T0 = trans[n][i]; T0; T0 = T0->nxt)
		for (k = HAS_UNLESS-1; k >= 0; k--)
		{	if (p = T0->escp[k])
			for (T1 = trans[n][p]; T1; T1 = T1->nxt)
			{	if (isthere(trans[n][i], T1->t_id))
					continue;
				T2 = cpytr(T1);
				T2->e_trans = p;
				T2->nxt = trans[n][i];
				trans[n][i] = T2;
		}	}
	 } else /* outermost unless checked first */
	 {	T4 = T3 = (Trans *) 0;
		for (T0 = trans[n][i]; T0; T0 = T0->nxt)
		for (k = HAS_UNLESS-1; k >= 0; k--)
		{	if (p = T0->escp[k])
			for (T1 = trans[n][p]; T1; T1 = T1->nxt)
			{	if (isthere(trans[n][i], T1->t_id))
					continue;
				T2 = cpytr(T1);
				T2->nxt = (Trans *) 0;
				T2->e_trans = p;
				if (T3)	T3->nxt = T2;
				else	T4 = T2;
				T3 = T2;
		}	}
		if (T4)
		{	T3->nxt = trans[n][i];
			trans[n][i] = T4;
		}
	 }
	}
#endif
#ifndef NOREDUCE
	for (i = 1; i < m; i++)
	{	if (a_cycles)
		{ /* moves through these states are visible */
	#if PROG_LAB>0 && defined(HAS_NP)
			if (progstate[n][i])
				goto degrade;
			for (T1 = trans[n][i]; T1; T1 = T1->nxt)
				if (progstate[n][T1->st])
					goto degrade;
	#endif
			if (accpstate[n][i] || visstate[n][i])
				goto degrade;
			for (T1 = trans[n][i]; T1; T1 = T1->nxt)
				if (accpstate[n][T1->st])
					goto degrade;
		}
		T1 = trans[n][i];
		if (!T1) continue;
		g = mark_safety(T1);	/* V3.3.1 */
		if (g < 0) goto degrade; /* global */
		/* check if mixing of guards preserves reduction */
		if (T1->nxt)
		{	k = 0;
			for (T0 = T1; T0; T0 = T0->nxt)
			{	if (!(T0->atom&8))
					goto degrade;
				for (aa = 0; aa < 2; aa++)
				{	j = srinc_set(T0->tpe[aa]);
					if (j >= GLOBAL && j != ALPHA_F)
						goto degrade;
					if (T0->tpe[aa]
					&&  T0->tpe[aa]
					!=  T1->tpe[0])
						k = 1;
			}	}
			/* g = 0;	V3.3.1 */
			if (k)	/* non-uniform selection */
			for (T0 = T1; T0; T0 = T0->nxt)
			for (aa = 0; aa < 2; aa++)
			{	j = srinc_set(T0->tpe[aa]);
				if (j != LOCAL)
				{	k = srunc(T0->tpe[aa], j);
					for (h = 0; h < 6; h++)
						if (T1->qu[h] == k
						&&  T1->ty[h] == j)
							break;
					if (h >= 6)
					{	T1->qu[g%6] = k;
						T1->ty[g%6] = j;
						g++;
			}	}	}
			if (g > 6)
			{	T1->qu[0] = 0;	/* turn it off */
				printf("pan: warning, line %d, ",
					srcln[i]);
			 	printf("too many stmnt types (%d)",
					g);
			  	printf(" in selection\n");
			  goto degrade;
			}
		}
		/* mark all options global if >=1 is global */
		for (T1 = trans[n][i]; T1; T1 = T1->nxt)
			if (!(T1->atom&8)) break;
		if (T1)
degrade:	for (T1 = trans[n][i]; T1; T1 = T1->nxt)
			T1->atom &= ~8;	/* mark as unsafe */
		/* can only mix 'r's or 's's if on same chan */
		/* and not mixed with other local operations */
		T1 = trans[n][i];
		if (!T1 || T1->qu[0]) continue;
		j = T1->tpe[0];
		if (T1->nxt && T1->atom&8)
		{ if (j == 5*DELTA)
		  {	printf("warning: line %d ", srcln[i]);
			printf("mixed condition ");
			printf("(defeats reduction)\n");
			goto degrade;
		  }
		  for (T0 = T1; T0; T0 = T0->nxt)
		  for (aa = 0; aa < 2; aa++)
		  if  (T0->tpe[aa] && T0->tpe[aa] != j)
		  {	printf("warning: line %d ", srcln[i]);
			printf("[%d-%d] mixed %stion ",
				T0->tpe[aa], j, 
				(j==5*DELTA)?"condi":"selec");
			printf("(defeats reduction)\n");
			printf("	'%s' <-> '%s'\n",
				T1->tp, T0->tp);
			goto degrade;
		} }
	}
#endif
	for (i = 1; i < m; i++)
	{	T2 = trans[n][i];
		if (!T2
		||  T2->nxt
		||  strncmp(T2->tp, ".(goto)", 7)
		||  !stopstate[n][i])
			continue;
		stopstate[n][T2->st] = 1;
	}
	if (state_tables && !verbose)
	{	if (dodot)
		{	char buf[256], *q = buf, *p = procname[n];
			while (*p != '\0')
			{	if (*p != ':')
				{	*q++ = *p;
				}
				p++;
			}
			*q = '\0';
			printf("digraph ");
			switch (Btypes[n]) {
			case I_PROC:  printf("init {\n"); break;
			case N_CLAIM: printf("claim_%s {\n", buf); break;
			case E_TRACE: printf("notrace {\n"); break;
			case N_TRACE: printf("trace {\n"); break;
			default:      printf("p_%s {\n", buf); break;
			}
			printf("size=\"8,10\";\n");
			printf("  GT [shape=box,style=dotted,label=\"%s\"];\n", buf);
		} else
		{	switch (Btypes[n]) {
			case I_PROC:  printf("init\n"); break;
			case N_CLAIM: printf("claim %s\n", procname[n]); break;
			case E_TRACE: printf("notrace assertion\n"); break;
			case N_TRACE: printf("trace assertion\n"); break;
			default:      printf("proctype %s\n", procname[n]); break;
		}	}
		for (i = 1; i < m; i++)
		{	reach[i] = 1;
		}
		tagtable(n, m, is, srcln, reach);
		if (dodot) printf("}\n");
	} else
	for (i = 1; i < m; i++)
	{	int nrelse;
		if (Btypes[n] != N_CLAIM)
		{	for (T0 = trans[n][i]; T0; T0 = T0->nxt)
			{	if (T0->st == i
				&& strcmp(T0->tp, "(1)") == 0)
				{	printf("error: proctype '%s' ",
						procname[n]);
		  			printf("line %d, state %d: has un",
						srcln[i], i);
					printf("conditional self-loop\n");
					pan_exit(1);
		}	}	}
		nrelse = 0;
		for (T0 = trans[n][i]; T0; T0 = T0->nxt)
		{	if (strcmp(T0->tp, "else") == 0)
				nrelse++;
		}
		if (nrelse > 1)
		{	printf("error: proctype '%s' state",
				procname[n]);
		  	printf(" %d, inherits %d", i, nrelse);
		  	printf(" 'else' stmnts\n");
			pan_exit(1);
	}	}
#if !defined(LOOPSTATE) && !defined(BFS_PAR)
	if (state_tables)
#endif
	do_dfs(n, m, is, srcln, reach, lpstate);

	if (!t_reverse)
	{	return;
	}
	/* process n, with m states, is=initial state -- reverse list */
	if (!state_tables && Btypes[n] != N_CLAIM)
	{	for (i = 1; i < m; i++)
		{	Trans *Tx = (Trans *) 0; /* list of escapes */
			Trans *Ty = (Trans *) 0; /* its tail element */
			T1 = (Trans *) 0; /* reversed list */
			T2 = (Trans *) 0; /* its tail */
			T3 = (Trans *) 0; /* remembers possible 'else' */

			/* find unless-escapes, they should go first */
			T4 = T5 = T0 = trans[n][i];
	#ifdef HAS_UNLESS
			while (T4 && T4->e_trans) /* escapes are first in orig list */
			{	T5 = T4;	  /* remember predecessor */
				T4 = T4->nxt;
			}
	#endif
			/* T4 points to first non-escape, T5 to its parent, T0 to original list */
			if (T4 != T0)		 /* there was at least one escape */
			{	T3 = T5->nxt;		 /* start of non-escapes */
				T5->nxt = (Trans *) 0;	 /* separate */
				Tx = T0;		 /* start of the escapes */
				Ty = T5;		 /* its tail */
				T0 = T3;		 /* the rest, to be reversed */
			}
			/* T0 points to first non-escape, Tx to the list of escapes, Ty to its tail */

			/* first tail-add non-escape transitions, reversed */
			T3 = (Trans *) 0;
			for (T5 = T0; T5; T5 = T4)
			{	T4 = T5->nxt;
	#ifdef HAS_UNLESS
				if (T5->e_trans)
				{	printf("error: cannot happen!\n");
					continue;
				}
	#endif
				if (strcmp(T5->tp, "else") == 0)
				{	T3 = T5;
					T5->nxt = (Trans *) 0;
				} else
				{	T5->nxt = T1;
					if (!T1) { T2 = T5; }
					T1 = T5;
			}	}
			/* T3 points to a possible else, which is removed from the list */
			/* T1 points to the reversed list so far (without escapes) */
			/* T2 points to the tail element -- where the else should go */
			if (T2 && T3)
			{	T2->nxt = T3;	/* add else */
			} else
			{	if (T3) /* there was an else, but there's no tail */
				{	if (!T1)	/* and no reversed list */
					{	T1 = T3; /* odd, but possible */
					} else		/* even stranger */
					{	T1->nxt = T3;
			}	}	}

			/* add in the escapes, to that they appear at the front */
			if (Tx && Ty) { Ty->nxt = T1; T1 = Tx; }

			trans[n][i] = T1;
			/* reversed, with escapes first and else last */
	}	}
	if (state_tables && verbose)
	{	printf("FINAL proctype %s\n", 
			procname[n]);
		for (i = 1; i < m; i++)
		for (T0 = trans[n][i]; T0; T0 = T0->nxt)
			crack(n, i, T0, srcln);
	}
}
void
imed(Trans *T, int v, int n, int j)	/* set intermediate state */
{	progstate[n][T->st] |= progstate[n][v];
	accpstate[n][T->st] |= accpstate[n][v];
	stopstate[n][T->st] |= stopstate[n][v];
	mapstate[n][j] = T->st;
}
void
tagtable(int n, int m, int is, short srcln[], uchar reach[])
{	Trans *z;

	if (is >= m || !trans[n][is]
	||  is <= 0 || reach[is] == 0)
		return;
	reach[is] = 0;
	if (state_tables)
	for (z = trans[n][is]; z; z = z->nxt)
	{	if (dodot)
			dot_crack(n, is, z);
		else
			crack(n, is, z, srcln);
	}

	for (z = trans[n][is]; z; z = z->nxt)
	{
#ifdef HAS_UNLESS
		int i, j;
#endif
		tagtable(n, m, z->st, srcln, reach);
#ifdef HAS_UNLESS
		for (i = 0; i < HAS_UNLESS; i++)
		{	j = trans[n][is]->escp[i];
			if (!j) break;
			tagtable(n, m, j, srcln, reach);
		}
#endif
	}
}

extern Trans *t_id_lkup[];

void
dfs_table(int n, int m, int is, short srcln[], uchar reach[], uchar lpstate[])
{	Trans *z;

	if (is >= m || is <= 0 || !trans[n][is])
		return;
	if ((reach[is] & (4|8|16)) != 0)
	{	if ((reach[is] & (8|16)) == 16)	/* on stack, not yet recorded */
		{	lpstate[is] = 1;
			reach[is] |= 8; /* recorded */
			if (state_tables && verbose)
			{	printf("state %d line %d is a loopstate\n", is, srcln[is]);
		}	}
		return;
	}
	reach[is] |= (4|16);	/* visited | onstack */
	for (z = trans[n][is]; z; z = z->nxt)
	{	t_id_lkup[z->t_id] = z;
#ifdef HAS_UNLESS
		int i, j;
#endif
		dfs_table(n, m, z->st, srcln, reach, lpstate);
#ifdef HAS_UNLESS
		for (i = 0; i < HAS_UNLESS; i++)
		{	j = trans[n][is]->escp[i];
			if (!j) break;
			dfs_table(n, m, j, srcln, reach, lpstate);
		}
#endif
	}
	reach[is] &= ~16; /* no longer on stack */
}
void
do_dfs(int n, int m, int is, short srcln[], uchar reach[], uchar lpstate[])
{	int i;
	dfs_table(n, m, is, srcln, reach, lpstate);
	for (i = 0; i < m; i++)
		reach[i] &= ~(4|8|16);
}
void
crack(int n, int j, Trans *z, short srcln[])
{	int i;

	if (!z) return;
	printf("	state %3d -(tr %3d)-> state %3d  ",
		j, z->forw, z->st);
	printf("[id %3d tp %3d", z->t_id, z->tpe[0]);
	if (z->tpe[1]) printf(",%d", z->tpe[1]);
#ifdef HAS_UNLESS
	if (z->e_trans)
		printf(" org %3d", z->e_trans);
	else if (state_tables >= 2)
	for (i = 0; i < HAS_UNLESS; i++)
	{	if (!z->escp[i]) break;
		printf(" esc %d", z->escp[i]);
	}
#endif
	printf("]");
	printf(" [%s%s%s%s%s] %s:%d => ",
		z->atom&6?"A":z->atom&32?"D":"-",
		accpstate[n][j]?"a" :"-",
		stopstate[n][j]?"e" : "-",
		progstate[n][j]?"p" : "-",
		z->atom & 8 ?"L":"G",
		PanSource, srcln[j]);
	for (i = 0; z->tp[i]; i++)
		if (z->tp[i] == '\n')
			printf("\\n");
		else
			putchar(z->tp[i]);
	if (verbose && z->qu[0])
	{	printf("\t[");
		for (i = 0; i < 6; i++)
			if (z->qu[i])
				printf("(%d,%d)",
				z->qu[i], z->ty[i]);
		printf("]");
	}
	printf("\n");
	fflush(stdout);
}
/* spin -a m.pml; cc -o pan pan.c; ./pan -D | dot -Tps > foo.ps; ps2pdf foo.ps */
void
dot_crack(int n, int j, Trans *z)
{	int i;

	if (!z) return;
	printf("	S%d -> S%d  [color=black", j, z->st);

	if (z->atom&6) printf(",style=dashed");
	else if (z->atom&32) printf(",style=dotted");
	else if (z->atom&8) printf(",style=solid");
	else printf(",style=bold");

	printf(",label=\"");
	for (i = 0; z->tp[i]; i++)
	{	if (z->tp[i] == '\\'
		&&  z->tp[i+1] == 'n')
		{	i++; printf(" ");
		} else
		{	putchar(z->tp[i]);
	}	}
	printf("\"];\n");
	if (accpstate[n][j]) printf("  S%d [color=red,style=bold];\n", j);
	else if (progstate[n][j]) printf("  S%d [color=green,style=bold];\n", j);
	if (stopstate[n][j]) printf("  S%d [color=blue,style=bold,shape=box];\n", j);
}

#ifdef VAR_RANGES
#define BYTESIZE	32	/* 2^8 : 2^3 = 256:8 = 32 */

typedef struct Vr_Ptr {
	char	*nm;
	uchar	vals[BYTESIZE];
	struct Vr_Ptr *nxt;
} Vr_Ptr;
Vr_Ptr *ranges = (Vr_Ptr *) 0;

void
logval(char *s, int v)
{	Vr_Ptr *tmp;

	if (v<0 || v > 255) return;
	for (tmp = ranges; tmp; tmp = tmp->nxt)
		if (!strcmp(tmp->nm, s))
			goto found;
	tmp = (Vr_Ptr *) emalloc(sizeof(Vr_Ptr));
	tmp->nxt = ranges;
	ranges = tmp;
	tmp->nm = s;
found:
	tmp->vals[(v)/8] |= 1<<((v)%8);
}

void
dumpval(uchar X[], int range)
{	int w, x, i, j = -1;

	for (w = i = 0; w < range; w++)
	for (x = 0; x < 8; x++, i++)
	{
from:		if ((X[w] & (1<<x)))
		{	printf("%d", i);
			j = i;
			goto upto;
	}	}
	return;
	for (w = 0; w < range; w++)
	for (x = 0; x < 8; x++, i++)
	{
upto:		if (!(X[w] & (1<<x)))
		{	if (i-1 == j)
				printf(", ");
			else
				printf("-%d, ", i-1);
			goto from;
	}	}
	if (j >= 0 && j != 255)
		printf("-255");
}

void
dumpranges(void)
{	Vr_Ptr *tmp;
	printf("\nValues assigned within ");
	printf("interval [0..255]:\n");
	for (tmp = ranges; tmp; tmp = tmp->nxt)
	{	printf("\t%s\t: ", tmp->nm);
		dumpval(tmp->vals, BYTESIZE);
		printf("\n");
	}
}
#endif
