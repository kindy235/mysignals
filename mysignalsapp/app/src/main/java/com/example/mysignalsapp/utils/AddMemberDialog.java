package com.example.mysignalsapp.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.entity.Member;

import java.util.Calendar;
import java.util.Locale;

public class AddMemberDialog extends DialogFragment {

    public interface OnMemberAddedListener {
        void onMemberAdded(Member member);
    }
    private OnMemberAddedListener listener;

    // New constructor
    public static AddMemberDialog newInstance(Member member) {
        AddMemberDialog dialog = new AddMemberDialog();
        Bundle args = new Bundle();
        args.putParcelable("member", member);
        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_member, null);

        // Find the EditText views in the dialog layout
        EditText nameEditText = dialogView.findViewById(R.id.editTextName);
        EditText surnameEditText = dialogView.findViewById(R.id.editTextSurname);
        EditText editTextPicture = dialogView.findViewById(R.id.editTextPicture);
        EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
        EditText editTextHeight = dialogView.findViewById(R.id.editTextHeight);
        EditText editTextWeight = dialogView.findViewById(R.id.editTextWeight);
        EditText editTextBirthday = dialogView.findViewById(R.id.editTextBirthday);
        editTextBirthday.setOnClickListener(v -> showDatePickerDialog(editTextBirthday));

        // Retrieve the Member object from arguments

        Member member = getArguments().getParcelable("member");

        // Set default values for the dialog fields if member is not null (edit mode)
        String dialogTitle;
        String dialogPositive;
        if (member != null) {
            nameEditText.setText(member.getName());
            surnameEditText.setText(member.getSurname());
            editTextPicture.setText(member.getPicture());
            editTextDescription.setText(member.getDescription());
            editTextHeight.setText(String.valueOf(member.getHeight()));
            editTextWeight.setText(String.valueOf(member.getWeight()));
            editTextBirthday.setText(member.getBirthday().split("T")[0]);
            dialogTitle = "Edit Member";
            dialogPositive = "Edit";
        }else {
            dialogTitle = "Add Member";
            dialogPositive = "Add";
        }

        builder.setView(dialogView)
                .setTitle(dialogTitle)
                .setPositiveButton(dialogPositive, (dialog, which) -> {
                    // Get the entered values from the EditText views
                    String name = nameEditText.getText().toString();
                    String surname = surnameEditText.getText().toString();
                    String picture = editTextPicture.getText().toString();
                    String description = editTextDescription.getText().toString();
                    int height = Integer.parseInt(editTextHeight.getText().toString());
                    int weight = Integer.parseInt(editTextWeight.getText().toString());
                    String birthday = editTextBirthday.getText().toString();

                    Member newMember = new Member(name, surname, picture, description, height, weight, birthday);
                    if (member!= null){
                        newMember.setId(member.getId());
                    }
                    // Notify the listener (UserDataFragment) about the new member
                    listener.onMemberAdded(newMember);
                })
                .setNegativeButton("Cancel", null);

        return builder.create();
    }

    public void setOnMemberAddedListener(OnMemberAddedListener listener){
        this.listener = listener;
    }

    // Show the DatePickerDialog
    private void showDatePickerDialog(EditText editTextBirthday) {
        // Get the current date as default in the DatePickerDialog
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create the DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                    editTextBirthday.setText(selectedDate);

                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }
}
