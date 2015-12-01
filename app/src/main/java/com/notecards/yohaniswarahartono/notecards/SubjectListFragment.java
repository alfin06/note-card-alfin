package com.notecards.yohaniswarahartono.notecards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SubjectListFragment extends Fragment {

    // Member variables
    private RecyclerView    SubjectRecyclerView; // Recycler View for subject list
    private SubjectAdapter  Adapter;              // Adapter

    /**********************************************************************************************/
    /*                                          Create View                                       */
    /**********************************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subject, container, false);

        SubjectRecyclerView = (RecyclerView) view.findViewById(R.id.subject_recycler_view);
        SubjectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUserInterface();

        return view;
    }

/*    //    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_add:
                mSubject = new Subject();
                mSubject.setTitle("Subject #" + addIndex);
                mNoteSingleton.addNoteCard(mSubject);
                mNoteCardTitle.setText(mNoteSingleton.showSubject());
                addIndex++;
                return true;

            case R.id.action_del:
                mNoteSingleton.deleteNoteCard(mSubject);
                mNoteCardTitle.setText(mNoteSingleton.showSubject());
                addIndex--;
                return true;

            default:
                return true;
        }
    } */

    /***************************************************************************/
    /*             Keep track the interface if user make some changes          */
    /***************************************************************************/
    @Override
    public void onResume() {
        super.onResume();
        updateUserInterface();
    }

    /***************************************************************************/
    /*                           Update the user interface                     */
    /***************************************************************************/
    private void updateUserInterface() {
        NoteSingleton lab = NoteSingleton.get();
        List<Subject> subject = lab.getSubjects();

        if (Adapter == null) {
            Adapter = new SubjectAdapter(subject);
            SubjectRecyclerView.setAdapter(Adapter);
        } else {
            Adapter.notifyDataSetChanged();
        }
    }

    /***************************************************************************/
    /*  Create the holder for each book and set up an action if the book       */
    /*  is clicked                                                             */
    /***************************************************************************/
    private class SubjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private TextView mTotalNoteCard;  // Total notecards
        private Subject mSubject;       // Book class
        private TextView mNoteCardTitle;  // Subject title
        private int addIndex = 10;
        private NoteSingleton mNoteSingleton;

        public SubjectHolder(View itemView) {
            super(itemView);
            mNoteCardTitle = (TextView) itemView.findViewById(R.id.note_card_title);
            mTotalNoteCard = (TextView) itemView.findViewById(R.id.total_note_card);
        }

        public void bindSubject(Subject notecard) {
            mSubject = notecard;
            mNoteCardTitle.setText(mSubject.getTitle());
            int total = 5;//(mBooks.getTotalChaptersFinished() * 100) / mBooks.getTotalChapter();
            mTotalNoteCard.setText("Total: " + total);
        }

        @Override
        public void onClick(View v) {
            Intent moveLayout = NoteCardListActivity.newIntent(getActivity(), mSubject.getSubjectId());
            startActivity(moveLayout);
        }
    }

    /***************************************************************************/
    /*                      Create the adapter for each book                   */
    /***************************************************************************/
    private class SubjectAdapter extends RecyclerView.Adapter<SubjectHolder> {
        private List<Subject> mSubjects; // Array for each book
        private Subject mSubject;
        private NoteSingleton mNoteSingleton;
        private TextView mNoteCardTitle;
        private int addIndex;

        public SubjectAdapter(List<Subject> notecards) {
            mSubjects = notecards;
            mNoteSingleton = NoteSingleton.get();
        }

        @Override
        public SubjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_subject, parent, false);
            mNoteCardTitle = (TextView) view.findViewById(R.id.note_card_title);
            return new SubjectHolder(view);
        }

        @Override
        public void onBindViewHolder(SubjectHolder holder, int position) {
            Subject notecard = mSubjects.get(position);
            holder.bindSubject(notecard);
        }

        @Override
        public int getItemCount() {
            return mSubjects.size();
        }
    }
}