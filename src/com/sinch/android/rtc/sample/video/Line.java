package com.sinch.android.rtc.sample.video;

/**
 * Created by pcandido on 07/10/2016.
 */

public class Line {
        private static final long serialVersionUID = 6601006766832473959L;
        public long id;
        public String glicose;
        public String pressao;
        public String dia;
        public String hora;
//        @Override
//        public String toString() {
//            return "Line{" + "nome=" + nome + '\'' + '}';
//        }

        public long getID(){
                return id;
        }

        public String getGlicose(){
                return glicose;
        }

        public String getPressao(){
                return pressao;
        }

        public String getDia(){
                return dia;
        }

        public String getHora(){
                return hora;
        }






        public void setID(long ID){
                this.id = ID;
        }


        public void setGlicose(String Glicose) {
                this.glicose = Glicose;
        }

        public void setPressao(String Pressao){
               this.pressao = Pressao;
        }

        public void setDia(String Dia){
                this.dia = Dia;
        }

        public void setHora(String Hora){
                this.hora = Hora;
        }


}

