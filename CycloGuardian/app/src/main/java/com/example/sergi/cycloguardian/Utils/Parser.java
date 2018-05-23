package com.example.sergi.cycloguardian.Utils;


import android.util.Log;

import com.example.sergi.cycloguardian.Messages.IncomingCameraMessage;
import com.example.sergi.cycloguardian.Messages.IncomingCameraMessageActionRecive;
import com.example.sergi.cycloguardian.Messages.IncomingCameraMessageActionState;
import com.example.sergi.cycloguardian.Messages.IncomingCameraMessageReply;
import com.example.sergi.cycloguardian.Messages.IncomingCameraMessageVideoReply;
import com.example.sergi.cycloguardian.Messages.IncomingCameraMessageVideoStart;

/**
 * Created by sergi on 12/03/2018.
 */

public class Parser {

    public IncomingCameraMessage parsearMensaje(byte[] cadena) {
        String cad = new String(cadena);
        String cadToken = cad.replace(" ", "");  //Quitamos los espacios existentes en la cadena
            //TODO mensaje REPLY
            if ((cadToken.contains("rval")) && (cadToken.contains("msg_id")) && (cadToken.contains("param")) &&
                    !(cadToken.contains("tmp"))) {

                    //Creamos el nuevo mensaje, llamando a su constructor
                    IncomingCameraMessageReply reply = new IncomingCameraMessageReply();
                    reply.parserMessage(cadToken);
                    return reply;
            }

        //TODO mensaje Petición Recibido
        if ((cadToken.contains("rval")) && (cadToken.contains("msg_id"))) {

            //Creamos el nuevo mensaje, llamando a su constructor
            IncomingCameraMessageActionRecive actRecive = new IncomingCameraMessageActionRecive();
            actRecive.parserMessage(cadToken);
            return actRecive;
        }

        //TODO mensaje acción realizada
        if ((cadToken.contains("type")) && (cadToken.contains("msg_id")) && (cadToken.contains("param"))) {

            //Creamos el nuevo mensaje, llamando a su constructor
            IncomingCameraMessageActionState actionState = new IncomingCameraMessageActionState();
            actionState.parserMessage(cadToken);
            Log.i("PARSER", actionState.getParam() + actionState.getType());
            return actionState;
        }

        //TODO mensaje video start
        if ((cadToken.contains("type")) && (cadToken.contains("msg_id"))) {

            //Creamos el nuevo mensaje, llamando a su constructor
            IncomingCameraMessageVideoStart videoStart = new IncomingCameraMessageVideoStart();
            videoStart.parserMessage(cadToken);
            return videoStart;
        }

        //TODO mensaje video reply
        if ((cadToken.contains("rval")) && (cadToken.contains("msg_id")) && (cadToken.contains("param")) &&
                (cadToken.contains("tmp"))) {

            //Creamos el nuevo mensaje, llamando a su constructor;
            IncomingCameraMessageVideoReply videoReply = new IncomingCameraMessageVideoReply();
            videoReply.parserMessage(cadToken);
            return videoReply;
        }

        //An error ocurred
        return null;

    }

    public String extractFileName(String ruta) {
        String fileName = null;
        String rutaSin = ruta.replace(" ", "");  //Quitamos los espacios existentes en la cadena
        String delims = "[/]";
        String[] tokens = rutaSin.split(delims);

        for (int i = 0; i < tokens.length; i++) {
            if(tokens[i].contains("100MEDIA")) {
                fileName = tokens[i+1].substring(0,tokens[i+1].length()-1);
            }
        }
        return fileName;
    }

    public String generateFileURL(String nameFile) {
        return Constants.DIRECCION_FILE + nameFile;
    }
}
