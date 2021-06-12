package com.example.pasrpl1024.utils;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pasrpl1024.AnimeDetail;
import com.example.pasrpl1024.Login;
import com.example.pasrpl1024.MainActivity;
import com.example.pasrpl1024.R;
import com.example.pasrpl1024.SeasonedAnime;
import com.example.pasrpl1024.WatchedAnime;
import com.example.pasrpl1024.WatchingAnime;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>{
    private static AnimeAdapter adapterNoWatch;
    private static AnimeAdapter adapterWatched;
    private static AnimeAdapter adapterWatching;

    public static AnimeAdapter getAdapter(AdapterType type) throws NullPointerException {
        switch (type) {
            case SEASONED_ANIME:
                if (adapterNoWatch == null) adapterNoWatch = new AnimeAdapter(AdapterType.SEASONED_ANIME);
                return adapterNoWatch;
            case WATCHED_ANIME:
                if (adapterWatched == null) adapterWatched = new AnimeAdapter(AdapterType.WATCHED_ANIME);
                return adapterWatched;
            case WATCHING_ANIME:
                if (adapterWatching == null) adapterWatching = new AnimeAdapter(AdapterType.WATCHING_ANIME);
                return adapterWatching;
        }
        throw new NullPointerException();
    }

    private static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final AdapterType type;
    private List<AnimeData> animeData;
    private List<AnimeUserData> animeUserData;
    private final List<String> strings = Arrays.asList("1", "2", "3");
    private final List<String> decs = Arrays.asList("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas gravida risus mi","Lorem ipsum dolor sit amet Nunc euismod.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas gravida risus mi, auctor lobortis purus rhoncus at. Nunc euismod.");

    private AnimeAdapter(AdapterType type) {
        this.type = type;
        switch (type) {
            case SEASONED_ANIME:
                DatabaseReference databaseReference = firebaseDatabase.getReference("animes");
                animeData = new ArrayList<>();
                Task<DataSnapshot> task = databaseReference.get();
                task.addOnSuccessListener(dataSnapshot -> {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        AnimeData dataAnime = snapshot.getValue(AnimeData.class);
                        assert dataAnime != null;
                        dataAnime.setId(snapshot.getKey());
                        animeData.add(dataAnime);
                    }
                    notifyDataSetChanged();
                });
                break;
            case WATCHED_ANIME:
                animeUserData = new ArrayList<>(MainActivity.getUserAnime().getAnimeData());
                for (Iterator<AnimeUserData> it = animeUserData.iterator(); it.hasNext(); ) {
                    AnimeUserData element = it.next();
                    if (element.getWatched() != element.getEpisode()) it.remove();
                }
                notifyDataSetChanged();
                break;
            case WATCHING_ANIME:
                animeUserData = new ArrayList<>(MainActivity.getUserAnime().getAnimeData());
                for (Iterator<AnimeUserData> it = animeUserData.iterator(); it.hasNext(); ) {
                    AnimeUserData element = it.next();
                    if (element.getWatched() == element.getEpisode()) it.remove();
                }
                notifyDataSetChanged();
                break;
            default:
                throw new NullPointerException();
        }
    }

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new AnimeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {
        ImageGetter getter;
        switch (type) {
            case SEASONED_ANIME:
                holder.title.setText((adapterNoWatch.animeData.get(position).getTitle().length() >= 20) ? adapterNoWatch.animeData.get(position).getTitle().substring(0, 20).concat("...") : adapterNoWatch.animeData.get(position).getTitle());
                holder.desc.setText((adapterNoWatch.animeData.get(position).getDesc().length() >= 204) ? adapterNoWatch.animeData.get(position).getDesc().substring(0, 204).concat("...") : adapterNoWatch.animeData.get(position).getDesc());
                holder.released.setText(adapterNoWatch.animeData.get(position).getReleasedOn());
                holder.watched.setText("Episode : " + adapterNoWatch.animeData.get(position).getEpisode());
                getter = new ImageGetter(adapterNoWatch.animeData.get(position).getImageLink(), holder.image);
                getter.execute();
                break;
            case WATCHED_ANIME:
            case WATCHING_ANIME:
                holder.title.setText((animeUserData.get(position).getTitle().length() >= 20) ? animeUserData.get(position).getTitle().substring(0, 20).concat("...") : animeUserData.get(position).getTitle());
                holder.desc.setText((animeUserData.get(position).getDesc().length() >= 204) ? animeUserData.get(position).getDesc().substring(0, 204).concat("...") : animeUserData.get(position).getDesc());
                holder.released.setText(animeUserData.get(position).getReleasedOn());
                holder.watched.setText("Episode watched : " + animeUserData.get(position).getWatched() + "/" + animeUserData.get(position).getEpisode());
                getter = new ImageGetter(animeUserData.get(position).getImageLink(), holder.image);
                getter.execute();
                break;
            default:
                holder.title.setText(strings.get(position));
                holder.desc.setText(decs.get(position));
                holder.image.setImageResource(R.drawable.back_arrow);
        }
    }

    @Override
    public int getItemCount() {
        switch (type) {
            case WATCHED_ANIME:
            case WATCHING_ANIME:
                return animeUserData.size();
            case SEASONED_ANIME:
                return animeData.size();
            default:
                return strings.size();
        }
    }

    class AnimeViewHolder extends  RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener, View.OnClickListener {
        private TextView title, desc, watched, released;
        private ImageView image;

        public AnimeViewHolder(@NonNull View itemView) {
            super(itemView);
            switch (type) {
                case SEASONED_ANIME:
                case WATCHED_ANIME:
                case WATCHING_ANIME:
                    title = itemView.findViewById(R.id.recycler_title);
                    desc = itemView.findViewById(R.id.recycler_description);
                    watched = itemView.findViewById(R.id.recycler_watched);
                    released = itemView.findViewById(R.id.recycler_released);
                    image = itemView.findViewById(R.id.recycler_photo);
                    itemView.setOnCreateContextMenuListener(this);
                    itemView.setOnClickListener(this);
                    break;
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            switch (type) {
                case SEASONED_ANIME:
                    menu.add(R.menu.no_watch_anime_menu, R.id.no_watch_watching, 1, "Add to Watching").setOnMenuItemClickListener(this);
                    menu.add(R.menu.no_watch_anime_menu, R.id.no_watch_watched, 2, "Add to Watched").setOnMenuItemClickListener(this);
                    break;
                case WATCHED_ANIME:
                    menu.add(R.menu.watched_anime_menu, R.id.watched_edit, 1, "Edit").setOnMenuItemClickListener(this);
                    menu.add(R.menu.watched_anime_menu, R.id.watched_remove, 2, "Remove").setOnMenuItemClickListener(this);
                    break;
                case WATCHING_ANIME:
                    menu.add(R.menu.watching_anime_menu, R.id.watching_edit, 1, "Edit").setOnMenuItemClickListener(this);
                    menu.add(R.menu.watching_anime_menu, R.id.watching_remove, 2, "Remove").setOnMenuItemClickListener(this);
                    break;
            }

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            AddOrRemoveFromFirebase<?> task;
            AnimeUserData animeGetData;
            AnimeData anime;
            ChangeEpisodeDialog dialog;
            switch (item.getItemId()) {
                case R.id.no_watch_watching:
                    break;
                case R.id.no_watch_watched:
                    if (MainActivity.getUserAnime() == null) {
                        Toast.makeText(SeasonedAnime.getSeasonedAnime(), "You're already logged out!", Toast.LENGTH_SHORT).show();
                        SeasonedAnime.getSeasonedAnime().startActivity(new Intent(SeasonedAnime.getSeasonedAnime(), Login.class));
                        return false;
                    }
                    anime = animeData.get(getAdapterPosition());
                    animeGetData = new AnimeUserData(anime.getTitle(),anime.getDesc(),anime.getReleasedOn(),anime.getEpisode(),anime.getImageLink(), anime.getPosterLink());
                    animeGetData.setWatched(anime.getEpisode());
                    animeGetData.setId(anime.getId());
                    task = new AddOrRemoveFromFirebase<>(animeGetData, adapterWatched, AddOrRemoveFromFirebase.ActionType.NOT_WATCHED_ADD_DATA);
                    task.execute();
                    break;
                case R.id.watched_edit:
                    if (MainActivity.getUserAnime() == null) {
                        Toast.makeText(WatchedAnime.getWatchedAnime(), "You're already logged out!", Toast.LENGTH_SHORT).show();
                        WatchedAnime.getWatchedAnime().startActivity(new Intent(WatchedAnime.getWatchedAnime(), Login.class));
                        return false;
                    }
                    dialog = new ChangeEpisodeDialog();
                    dialog.onCreateDialog(new Bundle(), WatchedAnime.getWatchedAnime(), episode -> {
                        AnimeUserData getData = adapterWatched.animeUserData.get(getAdapterPosition());
                        if (episode > getData.getEpisode()) return false;
                        getData.setWatched(episode);
                        for (Iterator<AnimeUserData> it = adapterWatched.animeUserData.iterator(); it.hasNext(); ) {
                            AnimeUserData currentData = it.next();
                            if (currentData.getWatched() < currentData.getEpisode()) {
                                it.remove();
                                adapterWatching.animeUserData.add(getData);
                            }

                        }
                        AddOrRemoveFromFirebase<AnimeUserData> taskAsync = new AddOrRemoveFromFirebase<>(getData, adapterWatched, AddOrRemoveFromFirebase.ActionType.WATCHED_EDIT_DATA);
                        taskAsync.execute();
                        return true;
                    }).show();
                    break;
                case R.id.watched_remove:
                    if (MainActivity.getUserAnime() == null) {
                        Toast.makeText(WatchedAnime.getWatchedAnime(), "You're already logged out!", Toast.LENGTH_SHORT).show();
                        WatchedAnime.getWatchedAnime().startActivity(new Intent(WatchedAnime.getWatchedAnime(), Login.class));
                        return false;
                    }
                    task = new AddOrRemoveFromFirebase<>(adapterWatched.animeUserData.remove(getAdapterPosition()), adapterWatched, AddOrRemoveFromFirebase.ActionType.WATCHED_REMOVE_DATA);
                    task.execute();
                    break;
                case R.id.watching_edit:
                    if (MainActivity.getUserAnime() == null) {
                        Toast.makeText(WatchedAnime.getWatchedAnime(), "You're already logged out!", Toast.LENGTH_SHORT).show();
                        WatchedAnime.getWatchedAnime().startActivity(new Intent(WatchedAnime.getWatchedAnime(), Login.class));
                        return false;
                    }
                    dialog = new ChangeEpisodeDialog();
                    dialog.onCreateDialog(new Bundle(), WatchingAnime.getWatchingAnime(), episode -> {
                        AnimeUserData getData = adapterWatching.animeUserData.get(getAdapterPosition());
                        if (episode > getData.getEpisode()) return false;
                        getData.setWatched(episode);
                        for (Iterator<AnimeUserData> it = adapterWatching.animeUserData.iterator(); it.hasNext(); ) {
                            AnimeUserData currentData = it.next();
                            if (currentData.getWatched() == currentData.getEpisode()) {
                                it.remove();
                                adapterWatched.animeUserData.add(getData);
                            }

                        }
                        AddOrRemoveFromFirebase<AnimeUserData> taskAsync = new AddOrRemoveFromFirebase<>(getData, adapterWatching, AddOrRemoveFromFirebase.ActionType.WATCHING_EDIT_DATA);
                        taskAsync.execute();
                        return true;
                    }).show();
                    break;
                case R.id.watching_remove:
                    if (MainActivity.getUserAnime() == null) {
                        Toast.makeText(WatchedAnime.getWatchedAnime(), "You're already logged out!", Toast.LENGTH_SHORT).show();
                        WatchedAnime.getWatchedAnime().startActivity(new Intent(WatchedAnime.getWatchedAnime(), Login.class));
                        return false;
                    }
                    task = new AddOrRemoveFromFirebase<>(adapterWatching.animeUserData.remove(getAdapterPosition()), adapterWatching, AddOrRemoveFromFirebase.ActionType.WATCHING_REMOVE_DATA);
                    task.execute();
                    break;
                default:
                    return false;
            }
            return false;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle;
            Intent intent;
            switch (type) {
                case SEASONED_ANIME:
                    if (MainActivity.getUserAnime() == null) {
                        Toast.makeText(SeasonedAnime.getSeasonedAnime(), "You're already logged out!", Toast.LENGTH_SHORT).show();
                        SeasonedAnime.getSeasonedAnime().startActivity(new Intent(SeasonedAnime.getSeasonedAnime(), Login.class));
                        return;
                    }
                    bundle = new Bundle();
                    bundle.putSerializable("data",animeData.get(getAdapterPosition()));
                    intent = new Intent(SeasonedAnime.getSeasonedAnime(), AnimeDetail.class).putExtras(bundle);
                    SeasonedAnime.getSeasonedAnime().startActivity(intent);
                    break;
                case WATCHING_ANIME:
                case WATCHED_ANIME:
                    if (MainActivity.getUserAnime() == null) {
                        Toast.makeText(WatchedAnime.getWatchedAnime(), "You're already logged out!", Toast.LENGTH_SHORT).show();
                        WatchedAnime.getWatchedAnime().startActivity(new Intent(WatchedAnime.getWatchedAnime(), Login.class));
                        return;
                    }
                    bundle = new Bundle();
                    bundle.putSerializable("data",animeUserData.get(getAdapterPosition()));
                    intent = new Intent(SeasonedAnime.getSeasonedAnime(), AnimeDetail.class).putExtras(bundle);
                    SeasonedAnime.getSeasonedAnime().startActivity(intent);
                    break;
            }
        }
    }


    static class AddOrRemoveFromFirebase<F> extends AsyncTask<String, Void, Boolean> {

        private final F data;
        private final AnimeAdapter adapter;
        private final ActionType actionType;
        private boolean result;

        public AddOrRemoveFromFirebase(F data, AnimeAdapter adapter, @NonNull ActionType actionType) {
            this.data = data;
            this.adapter = adapter;
            this.actionType = actionType;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            AnimeUserData userData;
            DatabaseReference dataAnimeNew;
            switch (actionType) {
                case WATCHING_REMOVE_DATA:
                case WATCHED_REMOVE_DATA:
                    try {
                        userData = (AnimeUserData) data;
                        dataAnimeNew = firebaseDatabase.getReference("usersWatchedAnime");
                        for (Iterator<AnimeUserData> it = MainActivity.getUserAnime().getAnimeData().iterator(); it.hasNext(); ) {
                            AnimeUserData iterator = it.next();
                            if (iterator.getId().equals(userData.getId())) it.remove();
                        }
                        dataAnimeNew.child(MainActivity.getUserAnime().getUser().getEmail()).child(userData.getId()).removeValue();
                        result = true;
                        return true;
                    } catch (Exception e) {
                        result = false;
                        return false;
                    }
                case NOT_WATCHED_ADD_DATA:
                    userData = (AnimeUserData) data;
                    for (AnimeUserData animeUserData : MainActivity.getUserAnime().getAnimeData()) {
                        if (animeUserData.getId().equals(userData.getId())) {
                            result = false;
                            return false;
                        }
                    }
                    MainActivity.getUserAnime().getAnimeData().add(userData);
                    adapter.animeUserData.add(userData);
                    for (AnimeUserData animeUserData : MainActivity.getUserAnime().getAnimeData()) {
                        String id = new String(animeUserData.getId());
                        animeUserData.setId(null);
                        DatabaseReference dataAnimeRef = firebaseDatabase.getReference("usersWatchedAnime");
                        Task<DataSnapshot> task = dataAnimeRef.child(MainActivity.getUserAnime().getUser().getEmail()).child(id).get();
                        task.addOnSuccessListener(dataSnap -> {
                            if (dataSnap.getValue() == null) {
                                dataAnimeRef.child(MainActivity.getUserAnime().getUser().getEmail()).child(id).setValue(animeUserData);
                            }
                        });
                        animeUserData.setId(id);
                    }
                    result = true;
                    return true;
                case WATCHING_EDIT_DATA:
                case WATCHED_EDIT_DATA:
                    userData = (AnimeUserData) data;
                    for (AnimeUserData animeUserData : MainActivity.getUserAnime().getAnimeData()) {
                        if (animeUserData.getId().equals(userData.getId())) animeUserData.setWatched(userData.getWatched());
                    }
                    for (AnimeUserData animeUserData : MainActivity.getUserAnime().getAnimeData()) {
                        String id = new String(animeUserData.getId());
                        animeUserData.setId(null);
                        DatabaseReference dataAnimeRef = firebaseDatabase.getReference("usersWatchedAnime");
                        Task<DataSnapshot> task = dataAnimeRef.child(MainActivity.getUserAnime().getUser().getEmail()).child(id).get();
                        task.addOnSuccessListener(dataSnap -> dataAnimeRef.child(MainActivity.getUserAnime().getUser().getEmail()).child(id).setValue(animeUserData));
                        animeUserData.setId(id);
                    }
                    result = true;
                    return true;
                default:
                    result = false;
                    return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            adapter.notifyDataSetChanged();
            switch (actionType) {
                case NOT_WATCHED_ADD_DATA:
                    if (result) Toast.makeText(SeasonedAnime.getSeasonedAnime(), "Added to watched", Toast.LENGTH_LONG).show();
                    else Toast.makeText(SeasonedAnime.getSeasonedAnime(), "Show already added!", Toast.LENGTH_LONG).show();
                    break;
                case WATCHED_REMOVE_DATA:
                    if (result) Toast.makeText(WatchedAnime.getWatchedAnime(), "Anime successfully removed", Toast.LENGTH_LONG).show();
                    else Toast.makeText(WatchedAnime.getWatchedAnime(), "Failed to remove anime!", Toast.LENGTH_LONG).show();
                    break;
                case WATCHING_EDIT_DATA:
                    if (result) Toast.makeText(WatchingAnime.getWatchingAnime(), "Anime successfully edited", Toast.LENGTH_LONG).show();
                    else Toast.makeText(WatchingAnime.getWatchingAnime(), "Failed to edit anime!", Toast.LENGTH_LONG).show();
                    break;
                case WATCHED_EDIT_DATA:
                    if (result) Toast.makeText(WatchedAnime.getWatchedAnime(), "Anime successfully edited", Toast.LENGTH_LONG).show();
                    else Toast.makeText(WatchedAnime.getWatchedAnime(), "Failed to edit anime!", Toast.LENGTH_LONG).show();
                    break;
                case WATCHING_REMOVE_DATA:
                    if (result) Toast.makeText(WatchingAnime.getWatchingAnime(), "Anime successfully removed", Toast.LENGTH_LONG).show();
                    else Toast.makeText(WatchingAnime.getWatchingAnime(), "Failed to remove anime!", Toast.LENGTH_LONG).show();
                    break;
            }
        }

        enum ActionType {
            WATCHED_REMOVE_DATA,
            NOT_WATCHED_ADD_DATA,
            WATCHED_EDIT_DATA,
            WATCHING_EDIT_DATA,
            WATCHING_REMOVE_DATA
        }

    }

    public enum AdapterType {
        SEASONED_ANIME,
        WATCHED_ANIME,
        WATCHING_ANIME;
    }

    public interface AdapterClickListener {
        boolean click(int episode);
    }

}
