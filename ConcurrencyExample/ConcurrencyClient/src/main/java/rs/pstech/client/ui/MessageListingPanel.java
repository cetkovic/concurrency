package rs.pstech.client.ui;

import rs.pstech.common.Message;

import javax.swing.*;

/**
 * Use for displaying list of Messages.
 * Created by predrag.stefanovic on 4/9/2014.
 */
public class MessageListingPanel extends JScrollPane {

    private DefaultListModel<Message> dataModel;
    private JList<Message> messagesList;

    public MessageListingPanel(int numberOfVisibleRows) {

        super();
        dataModel = new DefaultListModel<Message>();
        messagesList = new JList<Message>(dataModel);

        this.setViewportView(messagesList);
        messagesList.setVisibleRowCount(numberOfVisibleRows);
    }

    public void addMessage(Message message) {
        dataModel.addElement(message);
    }
}
