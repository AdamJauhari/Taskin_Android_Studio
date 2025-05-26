package uca.aidama.taskin.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import uca.aidama.taskin.R;
import uca.aidama.taskin.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupNavigationCards();
        
        return root;
    }
    
    private void setupNavigationCards() {
        binding.scheduleCardView.setOnClickListener(v -> navigateTo(R.id.navigation_schedule));
        binding.materialsCardView.setOnClickListener(v -> navigateTo(R.id.navigation_materials));
        binding.groupsCardView.setOnClickListener(v -> navigateTo(R.id.navigation_groups));
        binding.remindersCardView.setOnClickListener(v -> navigateTo(R.id.navigation_reminders));
    }
    
    private void navigateTo(int destinationId) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(destinationId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}