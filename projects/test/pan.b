	switch (t->back) {
	default: Uerror("bad return move");
	case  0: goto R999; /* nothing to undo */

		 /* CLAIM never_0 */
;
		;
		
	case 4: /* STATE 8 */
		;
		p_restor(II);
		;
		;
		goto R999;

		 /* PROC Interaction */
;
		;
		
	case 6: /* STATE 5 */
		;
		_m = unsend(now.App_ch);
		;
		goto R999;

	case 7: /* STATE 7 */
		;
		_m = unsend(now.App_ch);
		;
		goto R999;

	case 8: /* STATE 9 */
		;
		_m = unsend(now.App_ch);
		;
		goto R999;

	case 9: /* STATE 11 */
		;
		_m = unsend(now.App_ch);
		;
		goto R999;

	case 10: /* STATE 13 */
		;
		_m = unsend(now.App_ch);
		;
		goto R999;

	case 11: /* STATE 15 */
		;
		_m = unsend(now.App_ch);
		;
		goto R999;

	case 12: /* STATE 17 */
		;
		_m = unsend(now.App_ch);
		;
		goto R999;

	case 13: /* STATE 19 */
		;
		_m = unsend(now.App_ch);
		;
		goto R999;

	case 14: /* STATE 27 */
		;
		p_restor(II);
		;
		;
		goto R999;

		 /* PROC App */
	case 15: /* STATE 4 */
		sv_restor();
		goto R999;

	case 16: /* STATE 5 */
		;
		XX = 1;
		unrecv(now.App_ch, XX-1, 0, now.Int_event, 1);
		now.Int_event = trpt->bup.oval;
		;
		;
		goto R999;
;
		;
		
	case 18: /* STATE 7 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 19: /* STATE 11 */
		sv_restor();
		goto R999;

	case 20: /* STATE 12 */
		;
		XX = 1;
		unrecv(now.App_ch, XX-1, 0, now.Int_event, 1);
		now.Int_event = trpt->bup.oval;
		;
		;
		goto R999;
;
		;
		
	case 22: /* STATE 14 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 23: /* STATE 18 */
		sv_restor();
		goto R999;

	case 24: /* STATE 19 */
		;
		XX = 1;
		unrecv(now.App_ch, XX-1, 0, now.Int_event, 1);
		now.Int_event = trpt->bup.oval;
		;
		;
		goto R999;
;
		;
		
	case 26: /* STATE 21 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 27: /* STATE 25 */
		sv_restor();
		goto R999;

	case 28: /* STATE 26 */
		;
		XX = 1;
		unrecv(now.App_ch, XX-1, 0, now.Int_event, 1);
		now.Int_event = trpt->bup.oval;
		;
		;
		goto R999;
;
		;
		
	case 30: /* STATE 28 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 31: /* STATE 32 */
		sv_restor();
		goto R999;
;
		;
		
	case 33: /* STATE 35 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 34: /* STATE 39 */
		sv_restor();
		goto R999;
;
		;
		
	case 36: /* STATE 42 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 37: /* STATE 46 */
		sv_restor();
		goto R999;

	case 38: /* STATE 47 */
		;
		XX = 1;
		unrecv(now.App_ch, XX-1, 0, now.Int_event, 1);
		now.Int_event = trpt->bup.oval;
		;
		;
		goto R999;
;
		;
		
	case 40: /* STATE 49 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 41: /* STATE 53 */
		sv_restor();
		goto R999;

	case 42: /* STATE 54 */
		;
		XX = 1;
		unrecv(now.App_ch, XX-1, 0, now.Int_event, 1);
		now.Int_event = trpt->bup.oval;
		;
		;
		goto R999;
;
		;
		
	case 44: /* STATE 56 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 45: /* STATE 60 */
		sv_restor();
		goto R999;

	case 46: /* STATE 61 */
		;
		XX = 1;
		unrecv(now.App_ch, XX-1, 0, now.Int_event, 1);
		now.Int_event = trpt->bup.oval;
		;
		;
		goto R999;
;
		;
		
	case 48: /* STATE 63 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 49: /* STATE 67 */
		sv_restor();
		goto R999;
;
		;
		
	case 51: /* STATE 70 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 52: /* STATE 74 */
		sv_restor();
		goto R999;
;
		;
		
	case 54: /* STATE 77 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 55: /* STATE 81 */
		sv_restor();
		goto R999;
;
		;
		
	case 57: /* STATE 84 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 58: /* STATE 88 */
		sv_restor();
		goto R999;

	case 59: /* STATE 89 */
		;
		XX = 1;
		unrecv(now.App_ch, XX-1, 0, now.Int_event, 1);
		now.Int_event = trpt->bup.oval;
		;
		;
		goto R999;
;
		;
		
	case 61: /* STATE 91 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 62: /* STATE 95 */
		sv_restor();
		goto R999;
;
		;
		
	case 64: /* STATE 98 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 65: /* STATE 102 */
		sv_restor();
		goto R999;
;
		;
		
	case 67: /* STATE 105 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 68: /* STATE 109 */
		sv_restor();
		goto R999;
;
		;
		
	case 70: /* STATE 112 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 71: /* STATE 116 */
		sv_restor();
		goto R999;

	case 72: /* STATE 119 */
		;
		now.Int_event = trpt->bup.oval;
		;
		goto R999;

	case 73: /* STATE 126 */
		;
		now.Int_event = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 75: /* STATE 133 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 76: /* STATE 137 */
		sv_restor();
		goto R999;

	case 77: /* STATE 138 */
		;
		XX = 1;
		unrecv(now.App_ch, XX-1, 0, now.Int_event, 1);
		now.Int_event = trpt->bup.oval;
		;
		;
		goto R999;
;
		;
		
	case 79: /* STATE 140 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 80: /* STATE 144 */
		sv_restor();
		goto R999;
;
		;
		
	case 82: /* STATE 147 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 83: /* STATE 151 */
		sv_restor();
		goto R999;
;
		;
		
	case 85: /* STATE 154 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 86: /* STATE 158 */
		sv_restor();
		goto R999;
;
		;
		
	case 88: /* STATE 161 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 89: /* STATE 165 */
		sv_restor();
		goto R999;

	case 90: /* STATE 168 */
		;
		now.Int_event = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 92: /* STATE 175 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 93: /* STATE 179 */
		sv_restor();
		goto R999;

	case 94: /* STATE 182 */
		;
		now.Int_event = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 96: /* STATE 189 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 97: /* STATE 193 */
		sv_restor();
		goto R999;
;
		;
		
	case 99: /* STATE 196 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 100: /* STATE 200 */
		sv_restor();
		goto R999;
;
		;
		
	case 102: /* STATE 203 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 103: /* STATE 207 */
		sv_restor();
		goto R999;
;
		;
		
	case 105: /* STATE 210 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 106: /* STATE 214 */
		sv_restor();
		goto R999;

	case 107: /* STATE 217 */
		;
		now.Int_event = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 109: /* STATE 224 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 110: /* STATE 228 */
		sv_restor();
		goto R999;

	case 111: /* STATE 231 */
		;
		now.Int_event = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 113: /* STATE 238 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 114: /* STATE 242 */
		sv_restor();
		goto R999;
;
		;
		
	case 116: /* STATE 245 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 117: /* STATE 249 */
		sv_restor();
		goto R999;
;
		;
		
	case 119: /* STATE 252 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 120: /* STATE 256 */
		sv_restor();
		goto R999;
;
		;
		
	case 122: /* STATE 259 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 123: /* STATE 263 */
		sv_restor();
		goto R999;

	case 124: /* STATE 266 */
		;
		now.Int_event = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 126: /* STATE 273 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 127: /* STATE 277 */
		sv_restor();
		goto R999;
;
		;
		
	case 129: /* STATE 280 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 130: /* STATE 284 */
		sv_restor();
		goto R999;
;
		;
		
	case 132: /* STATE 287 */
		;
		now.App_event = trpt->bup.oval;
		;
		goto R999;
	case 133: /* STATE 291 */
		sv_restor();
		goto R999;

	case 134: /* STATE 294 */
		;
		now.Int_event = trpt->bup.oval;
		;
		goto R999;

	case 135: /* STATE 301 */
		;
		now.Int_event = trpt->bup.oval;
		;
		goto R999;

	case 136: /* STATE 307 */
		;
		now.flg_exit = trpt->bup.oval;
		;
		goto R999;

	case 137: /* STATE 308 */
		;
		p_restor(II);
		;
		;
		goto R999;
	}

