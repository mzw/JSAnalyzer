mtype = {
	_init,
	Nameless_48,
	onGood,
	getSeed,
	handleHttpGetSeed,
	validateLogin,
	tryLogin,
	alert_480,
	tryLogout,
	__exit__
};

mtype = {
	onclick,
	onmouseup,
	onComplete,
	onmousedown,
	onSuccess,
	onfocus,
	User_Click,
	onload,
	__empty__
};

mtype App_state, App_event, Int_event;
chan App_ch = [0] of { mtype };
bool flg_exit = false;

active proctype App() {
	d_step {
		Int_event = __empty__;
		App_event = __empty__;
		App_state = _init;
	}
goto__init:
	do
	:: App_ch?Int_event ->
		if
		:: Int_event == onload ->
			App_event = onload;
			d_step {
				Int_event = __empty__;
				App_event = __empty__;
				App_state = Nameless_48;
			}
goto_Nameless_48:
			do
			:: App_ch?Int_event ->
				if
				:: Int_event == onfocus ->
					App_event = onfocus;
					d_step {
						Int_event = __empty__;
						App_event = __empty__;
						App_state = getSeed;
					}
goto_getSeed:
					do
					:: App_ch?Int_event ->
						if
						:: Int_event == onSuccess ->
							App_event = onSuccess;
							d_step {
								Int_event = __empty__;
								App_event = __empty__;
								App_state = handleHttpGetSeed;
							}
goto_handleHttpGetSeed:
							do
							:: App_ch?Int_event ->
								if
								:: Int_event == onfocus ->
									App_event = onfocus;
									d_step {
										Int_event = __empty__;
										App_event = __empty__;
										App_state = getSeed;
									}
									goto goto_getSeed;
								:: Int_event == onfocus ->
									App_event = onfocus;
									d_step {
										Int_event = __empty__;
										App_event = __empty__;
										App_state = getSeed;
									}
									goto goto_getSeed;
								:: Int_event == onmousedown ->
									App_event = onmousedown;
									d_step {
										Int_event = __empty__;
										App_event = __empty__;
										App_state = validateLogin;
									}
goto_validateLogin:
									do
									:: App_ch?Int_event ->
										if
										:: Int_event == onComplete ->
											App_event = onComplete;
											d_step {
												Int_event = __empty__;
												App_event = __empty__;
												App_state = tryLogin;
											}
goto_tryLogin:
											do
											:: App_ch?Int_event ->
												if
												:: Int_event == User_Click ->
													App_event = User_Click;
													d_step {
														Int_event = __empty__;
														App_event = __empty__;
														App_state = alert_480;
													}
goto_alert_480:
													do
													:: App_ch?Int_event ->
														if
														:: Int_event == onfocus ->
															App_event = onfocus;
															d_step {
																Int_event = __empty__;
																App_event = __empty__;
																App_state = getSeed;
															}
															goto goto_getSeed;
														:: Int_event == onfocus ->
															App_event = onfocus;
															d_step {
																Int_event = __empty__;
																App_event = __empty__;
																App_state = getSeed;
															}
															goto goto_getSeed;
														:: Int_event == onmousedown ->
															App_event = onmousedown;
															d_step {
																Int_event = __empty__;
																App_event = __empty__;
																App_state = validateLogin;
															}
															goto goto_validateLogin;
														:: Int_event == onclick ->
															App_event = onclick;
															d_step {
																Int_event = __empty__;
																App_event = __empty__;
																App_state = onGood;
															}
goto_onGood:
															do
															:: App_ch?Int_event ->
																if
																:: Int_event == onfocus ->
																	App_event = onfocus;
																	d_step {
																		Int_event = __empty__;
																		App_event = __empty__;
																		App_state = getSeed;
																	}
																	goto goto_getSeed;
																:: Int_event == onfocus ->
																	App_event = onfocus;
																	d_step {
																		Int_event = __empty__;
																		App_event = __empty__;
																		App_state = getSeed;
																	}
																	goto goto_getSeed;
																:: Int_event == onmousedown ->
																	App_event = onmousedown;
																	d_step {
																		Int_event = __empty__;
																		App_event = __empty__;
																		App_state = validateLogin;
																	}
																	goto goto_validateLogin;
																:: Int_event == onclick ->
																	App_event = onclick;
																	d_step {
																		Int_event = __empty__;
																		App_event = __empty__;
																		App_state = onGood;
																	}
																	goto goto_onGood;
																:: else -> Int_event = __empty__;
																fi;
															od;
														:: else -> Int_event = __empty__;
														fi;
													od;
												:: Int_event == onmouseup ->
													App_event = onmouseup;
													d_step {
														Int_event = __empty__;
														App_event = __empty__;
														App_state = tryLogout;
													}
goto_tryLogout:
													do
													:: App_ch?Int_event ->
														if
														:: Int_event == onfocus ->
															App_event = onfocus;
															d_step {
																Int_event = __empty__;
																App_event = __empty__;
																App_state = getSeed;
															}
															goto goto_getSeed;
														:: Int_event == onfocus ->
															App_event = onfocus;
															d_step {
																Int_event = __empty__;
																App_event = __empty__;
																App_state = getSeed;
															}
															goto goto_getSeed;
														:: Int_event == onmousedown ->
															App_event = onmousedown;
															d_step {
																Int_event = __empty__;
																App_event = __empty__;
																App_state = validateLogin;
															}
															goto goto_validateLogin;
														:: Int_event == onclick ->
															App_event = onclick;
															d_step {
																Int_event = __empty__;
																App_event = __empty__;
																App_state = onGood;
															}
															goto goto_onGood;
														:: else -> Int_event = __empty__;
														fi;
													od;
												:: Int_event == onclick ->
													App_event = onclick;
													d_step {
														Int_event = __empty__;
														App_event = __empty__;
														App_state = onGood;
													}
													goto goto_onGood;
												:: else -> Int_event = __empty__;
												fi;
											od;
										:: Int_event == onfocus ->
											App_event = onfocus;
											d_step {
												Int_event = __empty__;
												App_event = __empty__;
												App_state = getSeed;
											}
											goto goto_getSeed;
										:: Int_event == onfocus ->
											App_event = onfocus;
											d_step {
												Int_event = __empty__;
												App_event = __empty__;
												App_state = getSeed;
											}
											goto goto_getSeed;
										:: Int_event == onmousedown ->
											App_event = onmousedown;
											d_step {
												Int_event = __empty__;
												App_event = __empty__;
												App_state = validateLogin;
											}
											goto goto_validateLogin;
										:: Int_event == onclick ->
											App_event = onclick;
											d_step {
												Int_event = __empty__;
												App_event = __empty__;
												App_state = onGood;
											}
											goto goto_onGood;
										:: else -> Int_event = __empty__;
										fi;
									od;
								:: Int_event == onclick ->
									App_event = onclick;
									d_step {
										Int_event = __empty__;
										App_event = __empty__;
										App_state = onGood;
									}
									goto goto_onGood;
								:: else -> Int_event = __empty__;
								fi;
							od;
						:: Int_event == onfocus ->
							App_event = onfocus;
							d_step {
								Int_event = __empty__;
								App_event = __empty__;
								App_state = getSeed;
							}
							goto goto_getSeed;
						:: Int_event == onfocus ->
							App_event = onfocus;
							d_step {
								Int_event = __empty__;
								App_event = __empty__;
								App_state = getSeed;
							}
							goto goto_getSeed;
						:: Int_event == onmousedown ->
							App_event = onmousedown;
							d_step {
								Int_event = __empty__;
								App_event = __empty__;
								App_state = validateLogin;
							}
							goto goto_validateLogin;
						:: Int_event == onclick ->
							App_event = onclick;
							d_step {
								Int_event = __empty__;
								App_event = __empty__;
								App_state = onGood;
							}
							goto goto_onGood;
						:: else -> Int_event = __empty__;
						fi;
					od;
				:: Int_event == onfocus ->
					App_event = onfocus;
					d_step {
						Int_event = __empty__;
						App_event = __empty__;
						App_state = getSeed;
					}
					goto goto_getSeed;
				:: Int_event == onmousedown ->
					App_event = onmousedown;
					d_step {
						Int_event = __empty__;
						App_event = __empty__;
						App_state = validateLogin;
					}
					goto goto_validateLogin;
				:: Int_event == onclick ->
					App_event = onclick;
					d_step {
						Int_event = __empty__;
						App_event = __empty__;
						App_state = onGood;
					}
					goto goto_onGood;
				:: else -> Int_event = __empty__;
				fi;
			od;
		:: else -> Int_event = __empty__;
		fi;
	od;
goto___exit__:
	flg_exit = true;
};

active proctype Interaction() {
	do ::
		if
		:: flg_exit -> break;
		:: else ->
			if
			:: skip -> App_ch!onload;
			:: skip -> App_ch!User_Click;
			:: skip -> App_ch!onfocus;
			:: skip -> App_ch!onSuccess;
			:: skip -> App_ch!onmousedown;
			:: skip -> App_ch!onComplete;
			:: skip -> App_ch!onmouseup;
			:: skip -> App_ch!onclick;
			fi;
		fi;
	od;
};

