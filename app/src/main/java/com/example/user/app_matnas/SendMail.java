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


//Class is extending AsyncTask because this class is going to perform a networking operation
public class SendMail extends AsyncTask<Void,Void,Void> {

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
    //Class Constructor
    public SendMail(Context context, String subject, String fname, String lname, String neighborhood, String phone, String email){
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

    public SendMail(Context context, String subject, String name, String phone, String message)
    {
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
        if(flag  == 0)
        progressDialog = ProgressDialog.show(context,"שולח את פרטי הרישום","נא להמתין...",false,false);
        else
        progressDialog = ProgressDialog.show(context,"שולח את ההודעה","נא להמתין...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();
        //Showing a success message
        if(flag == 0)
        Toast.makeText(context,"נרשמת בהצלחה",Toast.LENGTH_LONG).show();
        else
        Toast.makeText(context,"ההודעה נשלחה, יצרו איתך קשר תוך 24 שעות",Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
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
                        return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress(Config.EMAIL));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(Config.EMAIL));
            //Adding subject
            mm.setSubject(subject);
            //Adding content
            if(flag == 0)//todo form style
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