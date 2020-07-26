package com.dell.noteapp.adpater;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.dell.noteapp.R;
import com.dell.noteapp.database.AlarmReminderConstants;
import com.dell.noteapp.entity.Note;
import com.dell.noteapp.utils.UtilsHelper;
import com.dell.noteapp.view.NoteActivity;

import java.util.ArrayList;
// Class Khởi xử lý sự kiện và view cho từng phần tử trong danh sách
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    ArrayList<Note> noteList; // List danh sách note
    Context context;  // La interface Nó cho phép truy cập đến các tài nguyên và các lớp ứng dụng cụ thể.
    LayoutInflater inflater; // LayoutInflater là 1 component giúp bạn chuyển layout file(Xml) thành View(Java code) trong Android.
    UtilsHelper utilsHelper;  // Lớp chứa các phương thức truy vấn

    //Khởi tạo Contructer
    public NoteAdapter(Context context, ArrayList<Note> noteList) {
        super();
        this.context = context;
        this.noteList = noteList;
        inflater = LayoutInflater.from(context);
        utilsHelper = new UtilsHelper(context);
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ánh xạ  item cho list
        View view = inflater.inflate(R.layout.layout_item, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NoteHolder holder, final int position) {
        // Truyền data vào các view
        final Note note = noteList.get(position);
        holder.tvTitle.setText(note.getTitle() + "");
        // Check độ dài sâu truyền vào
        if (note.getContent().length() > 50) {
            String content = note.getContent().substring(0, 40) + "...";
            holder.tvContent.setText(content);
        } else {
            holder.tvContent.setText(note.getContent() + "");
        }

        holder.tvDate.setText(note.getDate());

        // Tab view hiện xem chi tiết item chọn
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NoteActivity.class);
                intent.putExtra("Note", note);
                intent.putExtra("option", "Update");
                Uri currentVehicleUri = ContentUris.withAppendedId(AlarmReminderConstants.AlarmReminderEntry.CONTENT_URI, position);
                // Set the URI on the data field of the intent
                intent.setData(currentVehicleUri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        // xóa item công việc
        holder.imageDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                builder.setTitle("Are you sure you want to delete this note?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        utilsHelper.deleteNote(note, "a");
                        removeItem(position);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
            }
        });

        //favorite
        if (note.isFavorite()) {
            // Set màu cho ô
            DrawableCompat.setTint(holder.imageFv.getDrawable(), ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            DrawableCompat.setTint(holder.imageFv.getDrawable(), ContextCompat.getColor(context, R.color.textFormatWhite));
        }

        if (note.isFinishTodo()) {
            // Set icon
            holder.checkFinistToDo.setImageResource(R.drawable.check);
        } else {
            // Set icon
            holder.checkFinistToDo.setImageResource(R.drawable.notcheck);
        }

        // Check công việc đã hoàn thành
        holder.checkFinistToDo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if (note.isFinishTodo()) {
                    // Set icon
                    holder.checkFinistToDo.setImageResource(R.drawable.notcheck);
                    note.setFinishTodo(false);
                    utilsHelper.finishTodo(note);
                } else {
                    // Set icon
                    holder.checkFinistToDo.setImageResource(R.drawable.check);
                    note.setFinishTodo(true);
                    utilsHelper.finishTodo(note);
                }
            }
        });


        holder.imageFv.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if (note.isFavorite()) {
                    holder.imageFv.setColorFilter(context.getResources().getColor(R.color.textFormatWhite));
                    note.setFavorite(false);
                    utilsHelper.updateFV(note);
                } else {
                    holder.imageFv.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
                    note.setFavorite(true);
                    utilsHelper.updateFV(note);
                }
            }
        });

        //Set time
        holder.timeSelect.setText(note.getTime());

        if (note.isCheckRepeat()) {
            // Set icon
            holder.activityNotifi.setImageResource(R.drawable.bell);
            note.setCheckRepeat(false);
            utilsHelper.updateFV(note);
        } else {
            holder.activityNotifi.setImageResource(R.drawable.ic_notifications_off_grey600_24dp);
            note.setCheckRepeat(true);
            utilsHelper.updateFV(note);
        }

        holder.activityNotifi.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if (note.isCheckRepeat()) {
                    holder.activityNotifi.setImageResource(R.drawable.bell);
                    note.setCheckRepeat(false);
                    utilsHelper.updateFV(note);
                } else {
                    holder.activityNotifi.setImageResource(R.drawable.ic_notifications_off_grey600_24dp);
                    note.setCheckRepeat(true);
                   // new AlarmScheduler().setAlarm(context, selectedTimestamp, mCurrentReminderUri);
                    utilsHelper.updateFV(note);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void removeItem(int position) {
        noteList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, noteList.size());
    }

    public class NoteHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvContent, tvDate;
        RelativeLayout layoutItem;
        ImageView imageFv, imageDel, checkFinistToDo;
        CardView cardView;
        private TextView mTitleText, mDateAndTimeText, timeSelect;
        private ImageView mActiveImage , activityNotifi;
        private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
        private TextDrawable mDrawableBuilder;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các biến
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            layoutItem = itemView.findViewById(R.id.layoutitem);
            imageFv = itemView.findViewById(R.id.imageFv);
            cardView = itemView.findViewById(R.id.cardviewitem);
            tvDate = itemView.findViewById(R.id.tvDate);
            imageDel = itemView.findViewById(R.id.imageDel);
            checkFinistToDo = itemView.findViewById(R.id.checkFinistToDo);

            timeSelect = (TextView) itemView.findViewById(R.id.time_select);
            activityNotifi = (ImageView) itemView.findViewById(R.id.active_image);


        }
    }

}
