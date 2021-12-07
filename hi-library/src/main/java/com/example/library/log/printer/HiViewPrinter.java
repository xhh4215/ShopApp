package com.example.library.log.printer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.R;
import com.example.library.log.HiLogConfig;
import com.example.library.log.HiLogMo;
import com.example.library.log.HiLogType;

import java.util.ArrayList;
import java.util.List;

/***
 * 将log显示在打印页面上
 */
public class HiViewPrinter implements HiLogPrinter {
    private RecyclerView recyclerView;
    private LogAdapter adapter;


    private HiViewPrinterProvider viewProvider;

    public HiViewPrinter(AppCompatActivity activity) {
        FrameLayout rootView = activity.findViewById(android.R.id.content);
        recyclerView = new RecyclerView(activity);
        adapter = new LogAdapter(LayoutInflater.from(recyclerView.getContext()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        viewProvider = new HiViewPrinterProvider(rootView, recyclerView);
    }

    public HiViewPrinterProvider getViewProvider() {
        return viewProvider;
    }

    @Override
    public void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String printString) {
        adapter.addItem(new HiLogMo(System.currentTimeMillis(), level, tag, printString));
        recyclerView.scrollToPosition(adapter.getItemCount()-1);
    }

    private static class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {
        private LayoutInflater inflater;
        private List<HiLogMo> logs = new ArrayList<>();

        public LogAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        void addItem(HiLogMo item) {
            logs.add(item);
            notifyItemInserted(logs.size() - 1);
        }

        @NonNull
        @Override
        public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.hilog_item, parent, false);

            return new LogViewHolder(itemView);
        }

        private int getHighlightColor(int logLevel) {
            int highLight;
            switch (logLevel) {
                case HiLogType.V:
                    highLight = 0xffbbbbbb;
                    break;
                case HiLogType.D:
                    highLight = 0xffffffff;
                    break;
                case HiLogType.I:
                    highLight = 0xff6a8759;
                    break;
                case HiLogType.E:
                    highLight = 0xffff6b68;
                    break;
                case HiLogType.W:
                    highLight = 0xffbbb529;
                    break;
                default:
                    highLight = 0xffffff00;
                    break;
            }
            return highLight;
        }

        @Override
        public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
            HiLogMo logMo = logs.get(position);
            int color = getHighlightColor(logMo.level);
            holder.tagView.setTextColor(color);
            holder.tagView.setText(logMo.getFlattened());
            holder.messageView.setTextColor(color);
            holder.messageView.setText(logMo.log);

        }

        @Override
        public int getItemCount() {
            return logs.size();
        }
    }

    private static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView tagView;
        TextView messageView;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            tagView = itemView.findViewById(R.id.tag);
            messageView = itemView.findViewById(R.id.message);
        }
    }
}
