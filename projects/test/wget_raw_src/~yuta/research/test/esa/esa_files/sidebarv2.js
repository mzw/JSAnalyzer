window._bugHerd = window._bugHerd||{};
window._bugHerd.data = window._bugHerd.data || {};
window._bugHerd.data.project_data = {"id":14099,"pusher_channel_name":"projectP30XS4sXtRwq8WUAduyXxA","name":"Easyservicedapartments.com","custom_brand":null};
window._bugHerd.url = window.location.protocol+"//www.bugherd.com";
window._bugHerd.sidebarJS = "https://34a7a1f9164c48c1b61f-84378afe43d6d6b04d77869b7b49ff0e.ssl.cf2.rackcdn.com/assets/sidebar-147f4a08958ab5bac2fe808fb0f69c88.js";
window._bugHerd.sidebarCSS = "https://34a7a1f9164c48c1b61f-84378afe43d6d6b04d77869b7b49ff0e.ssl.cf2.rackcdn.com/assets/sidebar-6f55109c03f24bf145afb176bc52c343.css";
window._bugHerd.guiCSS = "https://34a7a1f9164c48c1b61f-84378afe43d6d6b04d77869b7b49ff0e.ssl.cf2.rackcdn.com/assets/gui-3da3101bba6472c4d5adf4472bdc3857.css";
window._bugHerd.data.assets = {sidebar: "https://34a7a1f9164c48c1b61f-84378afe43d6d6b04d77869b7b49ff0e.ssl.cf2.rackcdn.com/assets/sidebar-147f4a08958ab5bac2fe808fb0f69c88.js"};
window._bugHerd.templates = "<script type=\"text/template\" id=\"eventlist-template\"></script>\n\n<script type=\"text/template\" id=\"searchbar-template\">\n  <div class=\"search-container\" style=\"z-index: 10000000\"></div>\n \n</script>\n\n<script type=\"text/template\" id=\"tasklist-template\"></script>\n\n<script type=\"text/template\" id=\"commentitem-template\">\n  <span class=\"itemInfo\" title=\"<%=  _.escape(user.get('email')) %>\"><%= _.escape(user.get('name')) %> posted &ndash; <%= $.timeago(created_at) %></span>\n  <div class=\"itemContent\">\n    <img src='<%= user.get('avatar') %>' width=\"32\" height=\"32\" />\n    <p class=\"value\"><%= _.textToSafeHTML(text) %></p>\n    <%if (commit_url){%>\n      <p class=\"value\"><a href=\"<%= commit_url %>\" target=\"_blank\">go to commit</a></p>\n    <%}%>\n  </div>\n</script>\n\n<script type=\"text/template\" id=\"eventitem-template\">\n  <div class=\"itemContent\">\n    <p class=\"value\">\n      <%= _.escape(user.get('name')) %> <em><%= _.textToSafeHTML(changes_html) %></em> &ndash; <%= $.timeago(created_at) %>\n    </p>\n  </div>\n</script>\n\n<!-- =================== -->\n<!-- ISSUE DETAILS PANEL -->\n<!-- =================== -->\n<script type=\"text/template\" id=\"detailPanel-template\">\n\n  <% if (bugherd.application.get('location') != 'admin') { %>\n  <div class=\"panelHead\">\n    <h2>Task Details</h2>\n    <ul class=\"panelHeadActions\">\n      <li class=\"miniButton button-closePanel\"><button tabindex=\"-1\">close</button></li>\n      <li class=\"miniButton loadingIcon\"></li>\n    </ul>\n  </div>\n  <% }  else if(false) { %>\n    <div class=\"panelHead\">\n      <div>Task Details      \n        <div class=\"detailDetach\" ><img src=\"https://bugherd-attachments-dev.s3.amazonaws.com/9wi8xbcr5cvkz3rsxo9j3g/32222282.jpg\" /></div>\n        \n        <div class=\"detailClose\" ><img src=\"https://secure.gravatar.com/avatar/c6a3b5893528e1f0845471484eb9dcc5?d=https://www.bugherd.com/images/sidebar/avatar-generic.png?s=32\" /></div>\n      </div>\n    </div>\n  <%}%>\n  <div class=\"taskDetails panelContent flexHeight\">\n    <div>\n      <div class=\"taskDetailMain\">\n        <div class=\"taskDetailMeta\">\n            \n          <span class=\"num taskDetailId\"><span class=\"value local_task_id\"></span></span>\n\n          <a data-status_id=\"null\" class=\"taskDetailState\">feedback</a>\n          <a data-status_id=\"0\" class=\"taskDetailState\">backlog</a>\n          <a data-status_id=\"1\" class=\"taskDetailState\">todo</a>\n          <a data-status_id=\"2\" class=\"taskDetailState\">doing</a>\n          <a data-status_id=\"4\" class=\"taskDetailState\">done</a>\n          <a data-status_id=\"5\" class=\"taskDetailState\">closed</a>\n\n          <time class=\"taskAge\"></time>\n          \n        </div>\n        <% if (bugherd.application.get('location') != 'admin') { %>\n        <b class=\"taskPriority\">\n          #<span class=\"value id\"></span>\n        </b>\n        <% } %>\n        <div title=\"click to edit description\" class=\"taskDescriptionHolder value\"></div>\n        <textarea title=\"&#8984; + &crarr;\" class=\"bh taskDescription\"></textarea>\n        <span class=\"count\"><strong>0</strong>/255</span>\n          <div class=\"taskDetailActions\">\n            <% if (bugherd.application.get('location') == 'admin') { %>\n            <div class=\"btn-group taskDetailSuppAction\">\n              <a class=\"btn btn-small dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">\n                <span class=\"btn-inner\">Action</span>\n                <span class=\"caret\"></span>\n              </a>\n              <ul class=\"dropdown-menu taskDetailSuppMenu pull-right\">\n                <li class=\"delete\"><a>delete</a></li>\n                <li class=\"divider\"></li>\n              </ul>\n            </div>\n            <% } %>\n            <span class=\"taskDetailMainAction splitButton\">\n              <% if (bugherd.application.get('mode') == 'reporter') { %>\n                <a class=\"button\" data-status_id=\"[null,0,1,2,4]\" data-set_id=\"5\">mark as closed</a>\n                <a class=\"button\" data-status_id=\"5\" data-set_id=\"null\">mark as open</a>\n                <span class=\"buttonActions\">\n                  <small>select an option</small>\n                  <ul class=\"taskDetailSubActions\">\n                    <li><a data-status_id=\"[null,0,1,2,4]\">open</a></li>\n                    <li><a data-status_id=\"5\">closed</a></li>\n                  </ul>\n                </span>\n              <% } else  { %>\n                <a class=\"button\" data-status_id=\"null\" data-set_id=\"0\">move to backlog</a>\n                <a class=\"button\" data-status_id=\"0\" data-set_id=\"1\">mark as todo</a>\n                <a class=\"button\" data-status_id=\"1\" data-set_id=\"2\">mark as doing</a>\n                <a class=\"button\" data-status_id=\"2\" data-set_id=\"4\">mark as done</a>\n                <a class=\"button\" data-status_id=\"4\" data-set_id=\"5\">mark as closed</a>\n                <a class=\"button\" data-status_id=\"5\" data-set_id=\"0\">move to backlog</a>\n                <span class=\"buttonActions\">\n                  <small>select an option</small>\n                  <ul class=\"taskDetailSubActions\">\n                    <li><a data-status_id=\"0\">backlog</a></li>\n                    <li><a data-status_id=\"1\">todo</a></li>\n                    <li><a data-status_id=\"2\">doing</a></li>\n                    <li><a data-status_id=\"4\">done</a></li>\n                    <li><a data-status_id=\"5\">closed</a></li>\n                  </ul>\n                </span>\n              <% } %>\n            </span>\n          </div>\n      </div>\n    </div>\n    <div class=\"flexHeight flexScroller\">\n      <div class=\"taskDetailContent\">\n        <div class=\"task-detail-infos\">\n          <div class=\"detailBlock screenshotBlock\">\n            <span class=\"label\">screenshot</span>\n            <div class=\"detailText screenshotMissing\">not available</div>\n            <a class=\"detailAction screenshotToggle\">view screenshot</a>\n            <div class=\"screenshotWindowHolder\" style=\"display:none\">\n              <div class=\"screenshotFlag\"/>\n              <img class=\"screenshotImage\"/>\n            </div>\n          </div>\n          <div class=\"detailBlock taskDetailRelativeUrlRow\">\n            <span class=\"label\">location</span>\n            <a class=\"taskDetailRelativeUrl\" target=\"_parent\"></a>\n          </div>\n          <div class=\"detailBlock detailBlockPriority\">\n            <span class=\"label\">severity</span>\n            <% if (bugherd.application.get('mode') == 'developer') { %>\n              <select class=\"taskDetailPriority\">\n                <option value=\"0\">not set</option>\n                <option value=\"1\">critical</option>\n                <option value=\"2\">important</option>\n                <option value=\"3\">normal</option>\n                <option value=\"4\">minor</option>\n              </select>\n            <% } else { %>\n              <span class=\"taskDetailPriority\"></span>\n            <% } %>\n          </div>\n          <div class=\"detailBlock detailBlockAssignee\">\n            <span class=\"label\">assigned to</span>\n            <% if (bugherd.application.get('mode') == 'developer') { %>\n              <select class=\"taskDetailAssignee\" style=\"width:200px;\" data-placeholder=\"unassigned\">\n                <option></option>\n              </select>\n            <% } else { %>\n              <span class=\"taskDetailAssignee value\"></span>\n            <% } %>\n          </div>\n          <div class=\"detailBlock\">\n            <span class=\"label\">reported by</span>\n            <span class=\"detailText taskDetailCreator\"></span>\n          </div>\n          <div class=\"detailBlock detailBlockTags\">\n            <span class=\"label\">tags</span>\n            <div class=\"detailText\" style=\"float:right; width:200px; text-align:left; position:relative;\">\n              <input id=\"detailTags\" type=\"text\" style=\"width:200px\" />\n            </div>\n          </div>\n          <div class=\"detailBlock attachBlock\">\n          \n            <span class=\"label\">attachments</span>\n          <a class=\"detailAction\" id=\"attachFile\" style='display:none;'>attach file</a>\n          <form class=\"attachmentUpload\" method=\"post\" enctype=\"multipart/form-data\">\n            <input type=\"file\" name=\"file\" class=\"attachButton\">\n          </form>\n          <ul class=\"attachList\"></ul>\n            <div class=\"detailProgressOut\" style=\"height:24px; width:100; padding: 3px;display:none;\"><div class=\"detailProgressBlock\" style=\"background:#718894; width:0%; height:100%; -webkit-border-radius: 3px 3px;\"></div></div>\n          </div>\n\n          <% if (bugherd.application.get('mode') == 'developer') { %>\n            <div class=\"detailBlock detailBlockDetails\">\n              <span class=\"label\">additional info</span>\n              <a class=\"detailAction detailToggle\">show details</a>\n              <table class=\"additionalInfo\">\n                <tr class=\"taskData taskDetailAbsoluteUrlRow\">\n                  <th>Task logged at: </th>\n                  <td><a class=\"taskDetailAbsoluteUrl\" target=\"_parent\"></a></td>\n                </tr>\n                <tr class=\"taskData\">\n                  <th>Operating System: </th>\n                  <td class=\"browserData-os value\"></td>\n                </tr>\n                <tr class=\"taskData\">\n                  <th>Browser: </th>\n                  <td class=\"browserData-browser value\"></td>\n                </tr>\n                <% if (bugherd.application.get('mode') == 'developer') { %>\n                <tr class=\"taskData\">\n                  <th>Selector:</th>\n                  <td class=\"taskDetailSelector selector_info value\"></td>\n                </tr>\n                <% } %>\n                <tr class=\"taskData\">\n                  <th>Resolution: </th>\n                  <td class=\"browserData-resolution value\"></td>\n                </tr>\n                <tr class=\"taskData\">\n                  <th>Browser Window: </th>\n                  <td class=\"browserData-window_size value\"></td>\n                  </tr>\n                <tr class=\"taskData\">\n                  <th>Color Depth: </th>\n                  <td class=\"browserData-colours value\"></td>\n                </tr>\n              </table>\n            </div>\n          <% } %>\n         \n        </div>\n        <div class=\"detailBlock activityBlock\">\n          <span class=\"label\">comments</span>\n        \n          <a class=\"detailAction activityToggle\">view log</a>\n          <div class=\"taskComment inactive\">\n            <textarea class=\"bh\" placeholder=\"click to add a comment\"></textarea>\n            <div class=\"commentActions\">\n              <button class=\"btn\">post comment</button> or <a class=\"cancelLink\">cancel</a>\n                <span class=\"count\"><strong>0</strong> / 255</span>\n            </div>\n            <div class=\"commentBlock\">\n         \n            </div>\n          </div>\n          <div class=\"eventBlock\" style=\"display:none\">\n            \n          </div>\n        </div>\n      </div>\n    </div>\n  </div>\n</script>\n\n<!-- =========================== -->\n<!-- SUB VIEW FOR USER META DATA -->\n<!-- =========================== -->\n<script type=\"text/template\" id=\"userMetaData-template\">\n  <tr class=\"taskData userMetaData\">\n    <th><%= key %>: </th>\n    <td class=\"value\"><%= value %></td>\n  </tr>\n</script>\n\n<!-- =============== -->\n<!-- ISSUE LIST ITEM -->\n<!-- =============== -->\n<script type=\"text/template\" id=\"taskitem-template\">\n  <div class=\"taskLink\">\n    <span class=\"taskID\"><%= local_task_id %></span>\n    <div class=\"taskContent p<%= priority_id %>\">\n      <b class=\"task-severity p<%= priority_id %>\"><%= priority_id %></b>\n      <p class=\"taskText truncated_description value\"><%= _.encodeHTML(description) %></p>\n      <span class=\"bhAlertBug\" style=\"display:none\">new activity</span>\n      <div class=\"task-actions\">\n        <% if (bugherd.application.get('location') == 'admin') { %>\n            <% if (status_id != 5) { %>\n              <span class=\"miniButton button-triageAccept\">\n                <a href=\"#\" title=\"move to task board\" class=\"backlog\">move to task board</a>\n              </span>\n            <% } else { %>\n              <span class=\"miniButton button-archiveDecline\">\n                <a href=\"#\" title=\"move to task board\" class=\"backlog\">move to task board</a>\n              </span>\n            <% } %>\n          <% } else { %>\n     \n        <% } %>\n      </div>\n    </div>\n  </div>\n</script>\n\n<!-- ======================= -->\n<!-- ISSUE LOGGING INTERFACE -->\n<!-- ======================= -->\n<script type=\"text/template\" id=\"taskcreate-template\">\n<div class=\"bh-entry-wrap\">\n<div class=\"bh-entry-inner-wrap\">\n<div class=\"bh-entry<% if (bugherd.application.get('mode') != 'developer') { %> entry-compact entry-basic<% } %>\">\n  <div class=\"entry-head\">\n    <h3></h3>\n    <a href=\"javascript:void(0);\" class=\"entry-close\">&times;</a>\n  </div>\n  <div class=\"entry-content\">\n    <div class=\"entry-description controls-row\">\n      <% if (bugherd.application.get('mode') == 'anonymous') { %>\n      <label class=\"inline\" for=\"bh-description\"><%= bugherd.getConfigOption('feedback','feedback_entry_placeholder') || 'write a comment or describe a problem' %></label>\n      <textarea id=\"bh-description\" /></textarea>\n      <% } else { %>\n      <textarea id=\"bh-description\" placeholder=\"write a comment or describe a problem\" /></textarea>\n      <% } %>\n    </div>\n    <% if (bugherd.application.get('mode') == 'developer') { %>\n    <div class=\"entry-details\">\n      <div class=\"entry-detail\">\n        <label>assignee</label> \n        <div class=\"assigneeHolder\">\n          <input id=\"bh-assignee\" type=\"text\" />\n        </div>\n      </div>\n      <div class=\"entry-detail\">\n        <label>severity</label>\n        <select id=\"bh-severity\">\n          <option value=\"0\">not set</option>\n          <option value=\"1\">critical</option>\n          <option value=\"2\">important</option>\n          <option value=\"3\">normal</option>\n          <option value=\"4\">minor</option>\n        </select>\n      </div>\n      <div class=\"entry-detail\">\n        <label>tags</label>\n        <div class=\"tagHolder\" >\n          <input id=\"bh-tags\" type=\"text\" />\n        </div>\n      </div>\n    </div>\n    <% } else { %>\n      <div class=\"reporter-wrap controls-row\">\n        <% if (bugherd.application.get('mode') != 'reporter') { %>\n        <label class=\"inline\" for=\"bh-reporter\"><%= bugherd.getConfigOption('feedback','feedback_email_placeholder') || 'your email address' %></label>\n        <input id=\"bh-reporter\" type=\"email\" value=\"<%= bugherd.getConfigOption('reporter','email') || bhReporter || \"\" %>\" maxlength=\"255\" />\n        <% } %>\n      </div>\n    <% } %>\n  </div>\n  <div class=\"entry-confirmation\">\n    <div class=\"confirm-message confirm-success\">\n        <span class=\"confirm-icon\"></span>\n        <% if (bugherd.application.get('mode') === 'anonymous'){ %>\n          <% if (bugherd.isExtension) { %>\n            <p>Here is a URL you can share with the site owner:</p>\n          <% } else { %>\n            <p><%= bugherd.getConfigOption('feedback','confirm_success_text') || \"Your feedback was sent.\" %></p>\n          <% } %>\n        <% } else { %>\n          <p>Task successfully added.</p>\n        <% } %>\n        <div class=\"extension-message\">\n            <%= bugherd.getConfigOption('feedback', 'confirm_extension_text') || \"Did you know you can send a screenshot with your bug reports?\" %> <span class=\"link linkHover\"><%= bugherd.getConfigOption('feedback','confirm_extension_link_text') || \"Find out how.\" %></span>\n        </div>\n        <br />\n        <button class=\"button-close button\"><%= bugherd.getConfigOption('feedback','confirm_close_text') || \"close\" %></button>\n      </div>\n      <div class=\"confirm-message confirm-loading\">\n        <span class=\"confirm-icon\"></span>\n        <p>\n          <% if (bugherd.application.get('mode') === 'anonymous'){ %>\n            <%= bugherd.getConfigOption('feedback','confirm_loading_text') || \"Sending feedback.\" %>\n          <% } else { %>\n            Saving feedback.\n          <% } %>\n        </p>\n      </div>\n      <div class=\"confirm-message confirm-error\">\n        <span class=\"confirm-icon\"></span>\n        <p>\n          <% if (bugherd.application.get('mode') === 'anonymous'){ %>\n            <%= bugherd.getConfigOption('feedback','confirm_error_text') || \"Sending feedback was unsuccessful.\" %>\n          <% } else { %>\n            Saving feedback was unsuccessful.\n          <% } %>\n        </p>\n        <br />\n        <button class=\"button-close button\"> <%= bugherd.getConfigOption('feedback','confirm_retry_text') || \"Try again\" %></button>\n      </div>\n  </div>\n  <div class=\"entry-actions\">\n    <% if (bugherd.application.get('location') == 'admin') { %>\n      <button class=\"button button-confirm button-admin\">create task</button>\n    <% } else if ( (bugherd.application.get('mode') == 'developer') || (bugherd.application.get('mode') == 'reporter') ) { %>\n      <button class=\"button button-confirm button-task\">create task</button>\n    <% } else if ((bugherd.application.get('mode') == 'anonymous') && (!bugherd.isUnsubscribed)) { %>\n      <button class=\"button button-confirm button-feedback\"><%= bugherd.getConfigOption('feedback','feedback_submit_text') || \"send feedback\" %></button>\n    <% } %>\n    <% /* <span class=\"action\">\n      <a href=\"#\" class=\"action-link\">tag a page element</a>\n    </span> */ %>\n  </div>\n  <% if (bugherd.application.get('mode') == 'anonymous') { %>\n    <% if (bugherd.application.get('customBrand')) { %>\n      <% if (bugherd.getConfigOption('brand','feedback_logo')) { %>\n      <span class=\"credit\" style=\"background:none;\"><img src=\"<%= bugherd.getConfigOption('brand','feedback_logo') %>\" alt=\"logo\" style=\"max-width:60px;max-height:60px;\" /></span>\n      <% } %>\n    <% } else { %>\n    <a href=\"http://www.bugherd.com/?utm_source=public&utm_medium=widget\" target=\"_blank\" tabindex=\"-1\" class=\"credit\">powered by <strong>BugHerd</strong></a>\n    <% } %>\n  <% } %>\n</div>\n</div>\n</div>\n</script>\n<!-- ================ -->\n<!-- ISSUE LIST PANEL -->\n<!-- ================ -->\n<script type=\"text/template\" id=\"listPanel-template\">\n  <div class=\"menuHolder\">\n    <div class=\"panelHead\">\n      <h2>Task List</h2>\n      <ul class=\"panelHeadActions\">\n        <li class=\"miniButton button-closePanel\"><button tabindex=\"-1\">close</button></li>\n        <li class=\"miniButton button-viewOptions\">\n          <span>view options</span>\n          <small><b>show options</b></small>\n          <ul class=\"panelViewOptions\">\n            <li><a class=\"sortLink\" data-sort-attr=\"priority_id\">severity</a></li>\n            <li><a class=\"sortLink\" data-sort-attr=\"created_at\">time created</a></li>\n            <li><a class=\"sortLink\" data-sort-attr=\"updated_at\">last modified</a></li>\n            <li><a class=\"sortLink\" data-sort-attr=\"assigned_to_id\">assigned to</a></li>\n            <li class=\"bordered\"><a class=\"orderLink opt-sortDirection\">inverse order</a></li>\n            <li><a class=\"currentPageLink opt-currentPage\">this page only</a></li>\n          </ul>\n        </li>\n      </ul>\n    </div>\n    <div class=\"panelPopup searchPopup searchbarHolder\"></div>\n    <div class=\"panelPopup alertsPopup unreadListHolder\"></div>\n    <div class='fixedHeader listHeading'></div>\n  </div>\n  <div class=\"nano listHolder flexHeight\">\n  <div class=\"content\" style=\"position: absolute; overflow: scroll; overflow-x: hidden; top: 0; bottom: 0; left: 0; width: 350px;\"></div>\n  </div>\n</script>\n\n<script type=\"text/template\" id=\"unreadtaskevent-template\">\n  <%= user.name %> <%= changes_html %> &ndash; <%= $.timeago(created_at) %>\n</script>\n\n<script type=\"text/template\" id=\"unreadtask-template\">\n  <td class=\"alertId taskLink\">\n    <span class=\"p<%= priority_id %>\"><%= local_task_id %></span>\n  </td>\n  <td>\n    <dl class=\"unreadTaskEvents\">\n      <dt class=\"unreadTaskDescription\"><%= _.textToSafeHTML(description) %></dt>\n    </dl>\n  </td>\n</script>\n\n<script type=\"text/template\" id=\"unreadlist-template\">\n  <div class=\"popupActions\">\n    <span class=\"unreadTasksCount\"></span> tasks recently changed\n    <a class=\"clearAllButton\">clear all</a>\n  </div>\n  <div class=\"alertList empty\">\n    <table class=\"unreadTasks\"></table>\n  </div>\n</script>\n\n<script type=\"text/template\" id=\"sidebar-template\">\n  <div class=\"mainNav\">\n    <% if (bugherd.application.get('customBrand')) { %>\n      <% if (bugherd.getConfigOption('brand','sidebar_logo')) { %>\n      <img src=\"<%= bugherd.getConfigOption('brand','sidebar_logo') %>\" alt=\"logo\" style=\"max-width:60px;max-height:60px;margin: 10px 5px 0;\" />\n      <% } %>\n    <% } else { %>\n    <h1><a target=\"_blank\" href=\"http://www.bugherd.com/projects\">BugHerd</a></h1>\n    <% } %>\n    <ul class=\"sidebarNav\">\n      <% if (bugherd.application.get('mode') == 'developer') { %>\n        <!-- Basic developer view: Show issues assigned to me, issues I'm watching, and my team's issues -->\n        <li class=\"navItem topFilter active nav-mine\" data-owner=\"mine\"><h4 class=\"navUserName navUser\" data-tooltip=\"tasks assigned to you\"><span>mine</span></h4>\n          <ul class=\"navStates\">\n            <li class=\"navStateItem subFilter\" data-status_id=\"0\">backlog <strong class=\"value-counter\"></strong></li>\n            <li class=\"navStateItem subFilter\" data-status_id=\"1\">todo <strong class=\"value-counter\"></strong></li>\n            <li class=\"navStateItem subFilter\" data-status_id=\"2\">doing <strong class=\"value-counter\"></strong></li>\n          </ul>\n        </li>\n        <li class=\"navItem topFilter nav-all\" data-owner=\"all\"><h4 class=\"navUserName navAll\" data-tooltip=\"tasks for your team\"><span>all</span></h4>\n          <ul class=\"navStates\">\n            <li class=\"navStateItem subFilter\" data-status_id=\"0\">backlog <strong class=\"value-counter\"></strong></li>\n            <li class=\"navStateItem subFilter\" data-status_id=\"1\">todo <strong class=\"value-counter\"></strong></li>\n            <li class=\"navStateItem subFilter\" data-status_id=\"2\">doing <strong class=\"value-counter\"></strong></li>\n            <li class=\"navStateItem subFilter\" data-status_id=\"4\">done <strong class=\"value-counter\"></strong></li>\n          </ul>\n        </li>\n      <% } else { %>\n        <!-- Reporter view: Show issues created by me, grouped into either open or closed -->\n        <li class=\"navItem topFilter active nav-reported\" data-owner=\"reported\"><h4 class=\"navUserName navReported\" data-tooltip=\"issues  reported by you\"><span>mine</span></h4>\n          <ul class=\"navStates\">\n            <li class=\"navStateItem subFilter\" data-status_id=\"[null,0,1,2,4]\">open <strong class=\"value-counter\"></strong></li>\n            <li class=\"navStateItem subFilter\" data-status_id=\"5\">closed <strong class=\"value-counter\"></strong></li>\n          </ul>\n        </li>\n      <% } %>\n    </ul>\n    <ul class=\"navNewUL\">\n      <li class=\"navItem\">\n        <h4 class=\"navUserName navNew\" data-tooltip=\"log a new issue\"><span>new issue</span></h4>\n      </li>\n    </ul>\n  </div>\n</script>\n\n<script type=\"text/template\" id=\"anonymousbar-template\">\n  <div class=\"feedbackTab\" <%= (bugherd.getConfigOption('feedback','hide')) ? 'style=\"display:none;\"' : '' %>> <%= bugherd.getConfigOption('feedback','tab_text') || \"Send Feedback\" %></div>\n  <div class=\"bh-entry-wrap\" style=\"display:none;\">\n    <div class=\"bh-entry-inner-wrap\">\n      <div class=\"bh-entry entry-compact\">\n        <div class=\"entry-head\">\n          <h3><%= bugherd.getConfigOption('feedback','option_title_text') || \"Choose an option\" %></h3>\n          <a href=\"javascript:void(0);\" class=\"entry-close\">&times;</a>\n        </div>\n        <div class=\"entry-content\">\n          <a href=\"#\" class=\"entry-option option-pin\"><%= bugherd.getConfigOption('feedback','option_pin_text') || \"I have feedback regarding a specific part of this page.\" %></a>\n          <a href=\"#\" class=\"entry-option option-site\"><%= bugherd.getConfigOption('feedback','option_site_text') || \"I have feedback regarding this page or site as a whole.\" %></a>\n        </div>\n      </div>\n    </div>\n  </div>\n</script>\n\n<script type=\"text/template\" id=\"bugbar-template\">\n  <div class=\"bugbarActions\">\n    <a class=\"navBugPin active\" data-tooltip=\"tag an element\">tag element</a>\n    <a class=\"navBugPage\" data-tooltip=\"tag this page\">tag page</a>\n    <a class=\"navBugCancel\" data-tooltip=\"cancel issue tagging\">done</a>\n  </div>\n</script>\n\n<script type=\"text/template\" id=\"app-template\">\n  <div class=\"anonymousbarWrapper\"></div>\n  <div class=\"bugbarWrapper\"></div>\n  <div class=\"sidebar\">\n    <div class=\"sidebarWrapper\"></div>\n    <div class=\"listbarWrapper\"></div>\n    <div class=\"detailbarWrapper\"></div>\n  </div>\n  <ul class=\"navOpts\">\n    <li class=\"navItem navToggle\" data-tooltip=\"toggle the sidebar\">\n      <h4 class=\"navUserName\"><span>hide this</span></h4>\n    </li>\n    <!--<li class=\"navItem navSupport\" data-tooltip=\"email support\">\n      <h4 class=\"navUserName\"><a href=\"mailto:support@bugherd.com\">send a support email</a></h4>\n    </li>-->\n  </ul>\n  <div class=\"taskcreateWrapper\"</div>\n</script>\n";
// /app/views/sidebar/tasks/init.js.erb is injected right here
// providing us with bh_init_data and bh_url

(function(namespace,undefined){
"use strict";

// Globals
var
  bh = namespace._bugHerd = namespace._bugHerd || {},
  location = namespace.location;

// Compatibility
bh.data = bh.data || namespace.bh_init_data;
bh.widgetType = bh.widgetType || namespace.bh_widget_type;
bh.testing = bh.testing || namespace.bh_testing;
bh.url = bh.url || namespace.bh_url;
bh.apiKey = bh.apiKey || namespace._bugHerdAPIKey;
bh.sidebarJS = bh.sidebarJS;
bh.sidebarCSS = bh.sidebarCSS;

// Close the window
bh.close = function(){
  // window.close does not work by itself, you have to do this hack
  // http://productforums.google.com/d/msg/chrome/GjsCrvPYGlA/pdvPRzBA4WwJ
  window.open('', '_self', '');
  window.close();
};

// Load the html
bh.load = function(){
 //removed this check as it prevents the body==null from doing its thing for Opera
  // // Check
  // if ( bh.loading ) {
  //   return;
  // }
  // bh.loading = true;

  // Prepare
  var head = document.getElementsByTagName('head')[0];
  var body = document.getElementsByTagName('body')[0];

  // Check
  if ( body == null) {
    window.setTimeout(bh.load, 23);
    return;
  }

  var bhNotInParent;
  try {
    if (window.top.document.getElementById('_BH_frame')) {
      bhNotInParent = true;
    }
    else {
      bhNotInParent = false;
    }
  }
  catch(ex) {
    bhNotInParent = false;
  }


  // Create iframe
  if (!document.getElementById('_BH_frame') && bhNotInParent === false) {
    // Inject the CSS
    var c = document.createElement('link');
    c.type = "text/css";
    c.rel = "stylesheet";
    c.href = bh.guiCSS;
    c.id = "_BH_CSS";
    head.appendChild(c);

    var iframe = document.createElement('iframe');

    iframe.setAttribute("style","display:block !important;border:0 !important;overflow:hidden" +
      "!important;position:absolute !important;right:0px !important;position:fixed !important;top:0px" +
      "!important;overflow:hidden !important;width:0%;background-color:transparent !important;");

    iframe.setAttribute('frameBorder', '0');
    iframe.setAttribute('border', '0');
    iframe.setAttribute('allowTransparency', 'true');
    iframe.setAttribute('id', '_BH_frame');
    iframe.setAttribute('name', '_BH_frame');
    iframe.setAttribute('scrolling', 'no');

    document.body.appendChild(iframe);

    // Apply
    bh.iframe = iframe;
    bh.win = iframe.contentWindow;
    bh.doc = bh.win.document;
    bh.doc.open().write('<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">' +
      '<html><head><title>BugHerd Sidebar</title><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=0"/><link href="https://fonts.googleapis.com/css?family=Open+Sans:400,600,700,300" rel="stylesheet" type="text/css"></head><body onload="parent.window._bugHerd.loaded()">' +
      bh.templates + '</body></html>');

    bh.doc.close();
    // Avoid webkit bug which scrolls infinite to the top margin of the iframe
    bh.win.scroll(0,0);

  } else {

    //if (bh.win.bugherd.isExtension === true && bh.win.bugherd.bugType == 'click') {
     // bh.win.bugherd.applicationView.subViews.anonymousbar.toggleOptions();
    //}
   // } else {
      // bh.win.bugherd.applicationView.subViews.showSomeNiceMessage();
    //}

  }
};

// Loaded
bh.loaded = function(){
  // Check
  if ( bh.loadingComplete ) {
    return;
  }
  bh.loadingComplete = true;

  // What to do when the page state changes
  var stateChange = function(){
    if ( bh.win.bugherd ) {
      bh.win.bugherd.stateChange();
    }
  };

  // Check the url for state changes
  var currentUrl = location.href;
  var locationInterval = setInterval(function(){
    if ( currentUrl !== location.href ) {
      currentUrl = location.href;
      stateChange();
    }
  },3000);

  // Check events for state changes
  if ( window.addEventListener ) {
    window.addEventListener('hashchange',stateChange);
    window.addEventListener('popstate',stateChange);
  }
  else if ( window.attachEvent ) {
    window.attachEvent('hashchange',stateChange);
    window.attachEvent('popstate',stateChange);
  }

  // Check history.js for state changes
  if ( window.History && window.History.Adapter ) {
    window.History.Adapter.bind(window,'statechange',stateChange);
    window.History.Adapter.bind(window,'anchorchange',stateChange);
  }

  // Inject Script
  var script = bh.doc.createElement('script');
  script.setAttribute('type', 'text/javascript');
  script.setAttribute('src', bh.sidebarJS);
  bh.doc.body.appendChild(script);

  // Inject more Styles
  var style = bh.doc.createElement('link');
  style.setAttribute('href', bh.sidebarCSS);
  style.setAttribute('type', 'text/css');
  style.setAttribute('rel', 'stylesheet');
  bh.doc.body.appendChild(style);
};

bh.load();

})(window);
;
