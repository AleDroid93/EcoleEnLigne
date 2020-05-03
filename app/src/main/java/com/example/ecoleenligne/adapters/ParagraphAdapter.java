package com.example.ecoleenligne.adapters;



import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.models.Paragraph;

import java.util.ArrayList;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class ParagraphAdapter extends RecyclerView.Adapter<ParagraphAdapter.ParagraphViewHolder>{
    private ArrayList<Paragraph> mParagraphs;
    private Context context;
    private boolean isResume;

    public ParagraphAdapter() {
        this.mParagraphs = new ArrayList<>();
        this.isResume = false;
    }

    @NonNull
    @Override
    public ParagraphViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.paragraph_item, parent, false);
        ParagraphAdapter.ParagraphViewHolder vh = new ParagraphViewHolder(view, this.context);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ParagraphViewHolder holder, int position) {
        Paragraph paragraph = mParagraphs.get(position);
        Integer paragraphNumber = paragraph.getNumber();
        String paragraphContent = "";
        if(isResume()) {
            paragraphContent = paragraph.getResume();
            holder.tvParagraphTitle.setVisibility(View.GONE);
        }else {
            String paragraphTitle = paragraph.getTitle();
            paragraphContent = paragraph.getContent();
            holder.tvParagraphTitle.setText(context.getString(R.string.paragraph, paragraphNumber, paragraphTitle));

        }
        holder.paragraphNumber = paragraphNumber;
        holder.tvParagraphContent.setText(paragraphContent);
    }

    @Override
    public int getItemCount() {
        return this.mParagraphs.size();
    }

    public ParagraphAdapter(ArrayList<Paragraph> paragraphs, boolean isResume, Context context) {
        this.mParagraphs = paragraphs;
        this.isResume = isResume;
        this.context = context;
    }


    public boolean isResume() {
        return isResume;
    }

    public void setResume(boolean resume) {
        isResume = resume;
    }

    public ArrayList<Paragraph> getParagraphs() {
        return mParagraphs;
    }

    public void setmParagraphs(ArrayList<Paragraph> paragraphs) {
        this.mParagraphs = paragraphs;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * ViewHolder Inner Class
     */
    public static class ParagraphViewHolder extends RecyclerView.ViewHolder {
        public TextView tvParagraphTitle;
        public TextView tvParagraphContent;
        public Integer paragraphNumber;
        public Context ctx;


        public ParagraphViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            tvParagraphTitle = itemView.findViewById(R.id.tv_paragraph);
            tvParagraphContent = itemView.findViewById(R.id.tv_paragraph_content);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tvParagraphContent.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            }
            this.ctx = ctx;
        }
    }
}
