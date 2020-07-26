package com.dell.noteapp.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dell.noteapp.database.DBNoteHelper;
import com.dell.noteapp.entity.Note;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

// Class chứa các phương thức truy vấn vào RoomDatabase
// Kế thừa các phương thức từ class UtilsQuery
public class UtilsHelper extends UtilsQuery {
    private  Context context;

    public UtilsHelper(Context context) {
        super(context);
        this.context= context;
    }


    // Hàm lưu lại giá trị trong DB
    @Override
    public void save(final Note note, final String title, final String content, final String txtdate,
                     final String txtTime, final String repeat, final String repeatInterval,
                     final boolean isRepeat, final String option) {
        class SaveNote extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                //Set các giá trị cho đối tượng
                note.setTitle(title);
                note.setContent(content);
                note.setDate(txtdate);
                note.setTime(txtTime);
                note.setRepeat(repeat);
                note.setRepeatInterval(repeatInterval);
                note.setCheckRepeat(isRepeat);
                if (option.equals("Update")) {
                    Log.e("NOTE", title + "-" + content + "-" + txtdate);
                    DBNoteHelper.getInstance(context).getNoteDatabase().getNoteDao().update(note);
                } else if (option.equals("Add")) {
                    DBNoteHelper.getInstance(context).getNoteDatabase().getNoteDao().insert(note);
                }
                return null;
            }

            //Hàm trả về
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ((Activity) context).finish();
            }
        }
        SaveNote saveNote = new SaveNote();
        saveNote.execute();
    }

    // Hàm xóa  giá trị trong DB
    @Override
    public void deleteNote(final Note note, final String m) {
        class DeleteNote extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DBNoteHelper.getInstance(context).getNoteDatabase()
                        .getNoteDao()
                        .delete(note);
                return null;
            }

            //Hàm trả ra giá trị
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show();
                if (m == "n") {
                    ((Activity) context).finish();
                }
            }
        }

        DeleteNote dt = new DeleteNote();
        dt.execute();
    }

    // Hàm update giá trị trong DB
    @Override
    public void updateFV(final Note note) {
        class UpdateFV extends AsyncTask<Void, Void, Void> {

            //Hàm xử lý tiến trình
            @Override
            protected Void doInBackground(Void... voids) {
                DBNoteHelper.getInstance(context).getNoteDatabase().getNoteDao().
                        updateFV(note.isFavorite(), note.getId());
                return null;
            }

            //Hàm trả về sau kết thúc
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }
        UpdateFV updateFV = new UpdateFV();
        updateFV.execute();
    }

    // Truy vấn lấy ra giá trị hoàn thành
    @Override
    public void finishTodo(final Note note) {
        class FinishTodo extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DBNoteHelper.getInstance(context).getNoteDatabase().getNoteDao().
                        insertFinish(note.isFinishTodo(), note.getId());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }
        FinishTodo finishTodo = new FinishTodo();
        finishTodo.execute();
    }

    // format form date
    public static String formatDate(String datefm, String format) throws ParseException {
        DateFormat dt = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH).parse(datefm);
        String dateFormat = dt.format(date);
        return dateFormat;
    }

    // format time
    public static int TimeToNow(String time) throws ParseException {
        Calendar cal1 = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();

        Date date = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH).parse(time);
        cal1.setTime(date);
        cal2.setTime(cal2.getTime());
        int result = daysBetween(cal2, cal1);
        return result;
    }

    // Tông giữa 2 time
    public static int daysBetween(Calendar day1, Calendar day2) {
        Calendar dayOne = (Calendar) day1.clone(),
                dayTwo = (Calendar) day2.clone();

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }
            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays;
        }
    }

    public String setUpDate(String date) throws ParseException {

        String dateSetUp = "";
        if (TimeToNow(date) == 0) {
            dateSetUp = "Today - " + formatDate(date, "HH:mm:ss a");
        }
        if (TimeToNow(date) == 1) {
            dateSetUp = "Yesterday - " + formatDate(date, "HH:mm:ss a");
        }
        if (TimeToNow(date) <= 7 && TimeToNow(date) > 1) {
            dateSetUp = formatDate(date, "EEEE") + " - " + formatDate(date, "HH:mm:ss a");
        }
        if (TimeToNow(date) > 7) {
            dateSetUp = formatDate(date, "dd/MM/yyyy") + " - " + formatDate(date, "HH:mm:ss a");
        }
        return dateSetUp;
    }

    public void convertTimeAgo(String time) {

        String timeAgo = "";
        try {
            SimpleDateFormat SDF = new SimpleDateFormat("E MMM d HH:mm:ss ZZZZ yyyy", Locale.US);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            String currentDate = SDF.format(Calendar.getInstance().getTime());
            Date past = SDF.parse(time);
            Date now = SDF.parse(currentDate);

            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if (seconds < 60) {
                timeAgo = "Vừa xong";
            } else if (minutes < 60) {
                timeAgo = minutes + " phút trước ";
            } else if (hours < 24) {
                timeAgo = hours + " giờ trước";
            } else if (days < 8) {
                timeAgo = days + " ngày trước";
            } else {
                timeAgo = format.format(past);
            }

            Log.d("zxcvbnm,.", timeAgo);
        } catch (Exception e) {
            Log.e("zxcvbnm,", e.getMessage());
        }
    }


}
