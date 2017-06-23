package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Belal on 10/30/2015.
 */


//Class is extending AsyncTask because this class is going to perform a networking operation
public class SendMail extends AsyncTask<Void, Void, Void> {

    //Declaring Variables
    private Context context;
    private Session session;

    //Information to send email
    private String subject;
    private String fname;
    private String lname;
    private String neighborhood;
    private String phone;
    private String email;
    private String name;
    private String message;
    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;

    private int flag = 0;

    //Class Constructor register form
    public SendMail(Context context, String subject, String fname, String lname, String neighborhood, String phone, String email) {
        //Initializing variables
        flag = 0;
        this.context = context;
        this.subject = subject;
        this.fname = fname;
        this.lname = lname;
        this.neighborhood = neighborhood;
        this.phone = phone;
        this.email = email;

    }

    //class constractor contact Us from
    public SendMail(Context context, String subject, String name, String phone, String message) {
        flag = 1;
        this.context = context;
        this.subject = subject;
        this.name = name;
        this.phone = phone;
        this.message = message;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        if (flag == 0)
            progressDialog = ProgressDialog.show(context, context.getString(R.string.sendRegister), context.getString(R.string.wait), false, false);
        else
            progressDialog = ProgressDialog.show(context, context.getString(R.string.sendMessage), context.getString(R.string.wait), false, false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();
        //Showing a success message
        if (flag == 0)
            Toast.makeText(context, R.string.successRegister, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, R.string.successContact, Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //Configuring properties for gmail
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(context.getString(R.string.mail), context.getString(R.string.p));
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress(context.getString(R.string.mail)));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(context.getString(R.string.p)));
            //Adding subject
            mm.setSubject(subject);
            //Adding content
            if (flag == 0)
                mm.setText(fname + "\n" + lname + "\n" + neighborhood + "\n" + phone + "\n" + email);
            else
                mm.setText(name + "\n" + phone + "\n" + message);
            //Sending email
            Transport.send(mm);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}