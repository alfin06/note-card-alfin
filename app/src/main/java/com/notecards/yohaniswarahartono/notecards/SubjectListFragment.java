package com.notecards.yohaniswarahartono.notecards;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

/**********************************************************************/
/*                          Subject List Fragment                     */
/**********************************************************************/
public class SubjectListFragment extends Fragment {
    // Constant Variables
    private static final String ADD_DIALOG      = "AddSubject";   // Tag for add subject dialog
    private static final String DELETE_DIALOG   = "DeleteDialog"; // Tag for add subject dialog
    private static final String SEND_SUBJECT_ID = "SubjectID";    // Tag to send subject id
    private static final int    REQUEST_CODE    = -1;             // Request Code for receive notification

    // Member variables
    private RecyclerView    SubjectRecyclerView; // Recycler View for subject list
    private SubjectAdapter  Adapter;             // Adapter

    /**********************************************************************************************/
    /*                                          Create View                                       */
    /**********************************************************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_subject, container, false);
            SubjectRecyclerView = (RecyclerView) view.findViewById(R.id.subject_recycler_view);
            SubjectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            onResume();
            return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.action_add:
                FragmentManager manager = getFragmentManager();
                DialogAddSubject dialog = new DialogAddSubject();
                dialog.setTargetFragment(this, REQUEST_CODE);
                dialog.show(manager, ADD_DIALOG);
                return true;

            case R.id.action_delete_all:
                FragmentManager       delete_manager = getFragmentManager();
                DialogDeleteAllSubject delete_dialog  = new DialogDeleteAllSubject();
                delete_dialog.setTargetFragment(this, REQUEST_CODE);
                delete_dialog.show(delete_manager, DELETE_DIALOG);
                return true;

            default:
                return true;
        }
    }

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
        }
        else {
            Adapter.notifyDataSetChanged();
        }
    }

    /***************************************************************************/
    /*                      Refresh the list after dialog                      */
    /***************************************************************************/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        onResume();
    }

    /***************************************************************************/
    /*  Create the holder for each book and set up an action if the book       */
    /*  is clicked                                                             */
    /***************************************************************************/
    private class SubjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        private TextView mSubjectTitle;  // Subject title
        private TextView mTotalNoteCard;  // Total note card
        private Button   mEditButton;     // Edit Button

        private Subject mSubject;         // Book class
        private NoteSingleton mNoteSingleton;

        public SubjectHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mSubjectTitle  = (TextView) itemView.findViewById(R.id.note_card_title);
            mTotalNoteCard = (TextView) itemView.findViewById(R.id.total_note_card);
            mEditButton    = (Button)   itemView.findViewById(R.id.edit_subject_name_button);
            mEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Set the arguments that will be passed to edit dialog fragment
                    Bundle subject_id = new Bundle();
                    subject_id.putSerializable(SEND_SUBJECT_ID, mSubject.getSubjectId());

                    // Setup the Dialog
                    FragmentManager manager = getFragmentManager();
                    DialogEditSubject editDialog = new DialogEditSubject();
                    editDialog.setArguments(subject_id);
                    editDialog.setTargetFragment(SubjectListFragment.this, REQUEST_CODE);
                    editDialog.show(manager, ADD_DIALOG);
                    onResume();
                }
            });
        }

        public void bindSubject(Subject subject) {
            mSubject = subject;
            mSubjectTitle.setText(mSubject.getTitle());
            int total = mSubject.getTotalNoteCard();
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

        private TextView mSubjectTitle;

        private List<Subject> mSubjects; // Array for each book
        private Subject mSubject;
        private NoteSingleton mNoteSingleton;
        private int addIndex;

        public SubjectAdapter(List<Subject> subjects) {
            mSubjects = subjects;
            mNoteSingleton = NoteSingleton.get();
        }

        @Override
        public SubjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view     = layoutInflater.inflate(R.layout.list_subject, parent, false);
            mSubjectTitle = (TextView) view.findViewById(R.id.note_card_title);

            return new SubjectHolder(view);
        }

        @Override
        public void onBindViewHolder(SubjectHolder holder, int position) {
            Subject noteCardSubject = mSubjects.get(position);
            holder.bindSubject(noteCardSubject);
        }

        @Override
        public int getItemCount() {
            return mSubjects.size();
        }
    }
}
