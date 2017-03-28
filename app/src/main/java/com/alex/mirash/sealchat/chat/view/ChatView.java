package com.alex.mirash.sealchat.chat.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.alex.mirash.sealchat.MainChatActivity;
import com.alex.mirash.sealchat.R;
import com.alex.mirash.sealchat.chat.adapter.AttachmentPreviewAdapter;
import com.alex.mirash.sealchat.chat.adapter.ChatAdapter;
import com.alex.mirash.sealchat.chat.util.chat.ChatHelper;
import com.alex.mirash.sealchat.chat.util.qb.PaginationHistoryListener;
import com.alex.mirash.sealchat.chat.util.qb.QbChatDialogMessageListenerImp;
import com.alex.mirash.sealchat.chat.util.qb.QbDialogUtils;
import com.alex.mirash.sealchat.chat.util.qb.VerboseQbChatConnectionListener;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * @author Mirash
 */

public class ChatView extends ScreenView<MainChatActivity> {
    private static final String TAG = "LOL";
    private static final int REQUEST_CODE_ATTACHMENT = 721;
    private static final int REQUEST_CODE_SELECT_PEOPLE = 752;

    private static final String PROPERTY_SAVE_TO_HISTORY = "save_to_history";

    public static final String EXTRA_DIALOG_ID = "dialogId";

    private ProgressBar progressBar;
    private StickyListHeadersListView messagesListView;
    private EditText messageEditText;

    private LinearLayout attachmentPreviewContainerLayout;

    private ChatAdapter chatAdapter;
    private AttachmentPreviewAdapter attachmentPreviewAdapter;
    private ConnectionListener chatConnectionListener;

    private QBChatDialog qbChatDialog;
    private ArrayList<QBChatMessage> unShownMessages;
    private int skipPagination = 0;
    private QbChatDialogMessageListenerImp chatMessageListener;


    public ChatView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        inflate(getContext(), R.layout.view_chat, this);
        messagesListView = (StickyListHeadersListView) findViewById(R.id.list_chat_messages);
        messageEditText = (EditText) findViewById(R.id.edit_chat_message);
        progressBar = (ProgressBar) findViewById(R.id.progress_chat);
        attachmentPreviewContainerLayout = (LinearLayout) findViewById(R.id.layout_attachment_preview_container);
        attachmentPreviewAdapter = new AttachmentPreviewAdapter(getActivity(),
                new AttachmentPreviewAdapter.OnAttachmentCountChangedListener() {
                    @Override
                    public void onAttachmentCountChanged(int count) {
                        attachmentPreviewContainerLayout.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
                    }
                },
                new AttachmentPreviewAdapter.OnAttachmentUploadErrorListener() {
                    @Override
                    public void onAttachmentUploadError(QBResponseException e) {
      /*                  showErrorSnackbar(0, e, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onAttachmentsClick(v);
                            }
                        });*/
                    }
                });
        AttachmentPreviewAdapterView previewAdapterView = (AttachmentPreviewAdapterView) findViewById(R.id.adapter_view_attachment_preview);
        previewAdapterView.setAdapter(attachmentPreviewAdapter);

        findViewById(R.id.button_chat_send).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSendChatClick();
            }
        });
    }

    public void showDialog(QBChatDialog dialog) {
        qbChatDialog = dialog;
        Log.d("LOL", "showDialog listeners:" + (qbChatDialog.getMessageListeners() != null
                ? qbChatDialog.getMessageListeners().size() : "empty"));
        qbChatDialog.initForChat(QBChatService.getInstance());
        chatMessageListener = new QbChatDialogMessageListenerImp() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
                showMessage(qbChatMessage);
            }
        };
        qbChatDialog.addMessageListener(chatMessageListener);
        switch (qbChatDialog.getType()) {
            case GROUP:
            case PUBLIC_GROUP:
                joinGroupChat();
                break;

            case PRIVATE:
                loadDialogUsers();
                break;

            default:
                Toaster.shortToast(String.format("%s %s", getActivity().getString(R.string.chat_unsupported_type), qbChatDialog.getType().name()));
                break;
        }
        initChatConnectionListener();
    }

/*
    @Override
    public void onResume() {
        super.onResume();
        ChatHelper.getInstance().addConnectionListener(chatConnectionListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        ChatHelper.getInstance().removeConnectionListener(chatConnectionListener);
    }
*/

    @Override
    public void onBackPressed() {
        releaseChat();
        super.onBackPressed();
    }

    public void handleSendChatClick() {
        int totalAttachmentsCount = attachmentPreviewAdapter.getCount();
        Collection<QBAttachment> uploadedAttachments = attachmentPreviewAdapter.getUploadedAttachments();
        if (!uploadedAttachments.isEmpty()) {
            if (uploadedAttachments.size() == totalAttachmentsCount) {
                for (QBAttachment attachment : uploadedAttachments) {
                    sendChatMessage(null, attachment);
                }
            } else {
                Toaster.shortToast(R.string.chat_wait_for_attachments_to_upload);
            }
        }

        String text = messageEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            sendChatMessage(text, null);
        }
    }

    public void showMessage(QBChatMessage message) {
        if (chatAdapter != null) {
            chatAdapter.add(message);
            scrollMessageListDown();
        } else {
            if (unShownMessages == null) {
                unShownMessages = new ArrayList<>();
            }
            unShownMessages.add(message);
        }
    }

    private void sendChatMessage(String text, QBAttachment attachment) {
        QBChatMessage chatMessage = new QBChatMessage();
        if (attachment != null) {
            chatMessage.addAttachment(attachment);
        } else {
            chatMessage.setBody(text);
        }
        chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
        chatMessage.setDateSent(System.currentTimeMillis() / 1000);
        chatMessage.setMarkable(true);

        if (!QBDialogType.PRIVATE.equals(qbChatDialog.getType()) && !qbChatDialog.isJoined()) {
            Toaster.shortToast("You're still joining a group chat, please wait a bit");
            return;
        }

        try {
            qbChatDialog.sendMessage(chatMessage);

            if (QBDialogType.PRIVATE.equals(qbChatDialog.getType())) {
                showMessage(chatMessage);
            }

            if (attachment != null) {
                attachmentPreviewAdapter.remove(attachment);
            } else {
                messageEditText.setText("");
            }
        } catch (SmackException.NotConnectedException e) {
            Log.w(TAG, e);
            Toaster.shortToast("Can't send a message, You are not connected to chat");
        }
    }

    private void joinGroupChat() {
        progressBar.setVisibility(View.VISIBLE);
        ChatHelper.getInstance().join(qbChatDialog, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle b) {
                loadDialogUsers();
            }

            @Override
            public void onError(QBResponseException e) {
                progressBar.setVisibility(View.GONE);
                Log.d("LOL", "joinGroupChat ERROR " + String.valueOf(e));
            }
        });
    }

    private void leaveGroupDialog() {
        try {
            ChatHelper.getInstance().leaveChatDialog(qbChatDialog);
        } catch (XMPPException | SmackException.NotConnectedException e) {
            Log.w(TAG, e);
        }
    }

    private void releaseChat() {
        qbChatDialog.removeMessageListrener(chatMessageListener);
        if (!QBDialogType.PRIVATE.equals(qbChatDialog.getType())) {
            leaveGroupDialog();
        }
    }

    private void loadDialogUsers() {
        ChatHelper.getInstance().getUsersFromDialog(qbChatDialog, new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> users, Bundle bundle) {
                setChatNameToActionBar();
                loadChatHistory();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d("LOL", "loadDialogUsers: " + e.toString());
            }
        });
    }

    private void setChatNameToActionBar() {
        ((ActionBar) getActivity().findViewById(R.id.chat_action_bar))
                .setTitleText(QbDialogUtils.getDialogName(qbChatDialog));
    }

    private void loadChatHistory() {
        ChatHelper.getInstance().loadChatHistory(qbChatDialog, skipPagination, new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> messages, Bundle args) {
                // The newest messages should be in the end of list,
                // so we need to reverse list to show messages in the right order
                Collections.reverse(messages);
                if (chatAdapter == null) {
                    chatAdapter = new ChatAdapter(getActivity(), qbChatDialog, messages);
                    chatAdapter.setPaginationHistoryListener(new PaginationHistoryListener() {
                        @Override
                        public void downloadMore() {
                            loadChatHistory();
                        }
                    });
                    chatAdapter.setOnItemInfoExpandedListener(new ChatAdapter.OnItemInfoExpandedListener() {
                        @Override
                        public void onItemInfoExpanded(final int position) {
                            if (isLastItem(position)) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        messagesListView.setSelection(position);
                                    }
                                });
                            } else {
                                messagesListView.smoothScrollToPosition(position);
                            }
                        }

                        private boolean isLastItem(int position) {
                            return position == chatAdapter.getCount() - 1;
                        }
                    });
                    if (unShownMessages != null && !unShownMessages.isEmpty()) {
                        List<QBChatMessage> chatList = chatAdapter.getList();
                        for (QBChatMessage message : unShownMessages) {
                            if (!chatList.contains(message)) {
                                chatAdapter.add(message);
                            }
                        }
                    }
                    messagesListView.setAdapter(chatAdapter);
                    messagesListView.setAreHeadersSticky(false);
                    messagesListView.setDivider(null);
                } else {
                    chatAdapter.addList(messages);
                    messagesListView.setSelection(messages.size());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(QBResponseException e) {
                progressBar.setVisibility(View.GONE);
                skipPagination -= ChatHelper.CHAT_HISTORY_ITEMS_PER_PAGE;
                Log.d("LOL", "loadChatHistory ERROR " + e.toString());
            }
        });
        skipPagination += ChatHelper.CHAT_HISTORY_ITEMS_PER_PAGE;
    }

    private void scrollMessageListDown() {
        messagesListView.setSelection(messagesListView.getCount() - 1);
    }

    private void initChatConnectionListener() {
        chatConnectionListener = new VerboseQbChatConnectionListener(this) {
            @Override
            public void reconnectionSuccessful() {
                super.reconnectionSuccessful();
                skipPagination = 0;
                switch (qbChatDialog.getType()) {
                    case GROUP:
                        chatAdapter = null;
                        // Join active room if we're in Group Chat
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                joinGroupChat();
                            }
                        });
                        break;
                }
            }
        };
        ChatHelper.getInstance().addConnectionListener(chatConnectionListener);
    }
}
