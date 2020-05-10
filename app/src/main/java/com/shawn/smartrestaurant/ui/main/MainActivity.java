package com.shawn.smartrestaurant.ui.main;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shawn.smartrestaurant.Code;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.AppDatabase;
import com.shawn.smartrestaurant.db.entity.Dish;
import com.shawn.smartrestaurant.db.entity.Other;
import com.shawn.smartrestaurant.db.entity.User;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;
import com.shawn.smartrestaurant.ui.login.LoginActivity;
import com.shawn.smartrestaurant.ui.main.addmenu.FragmentAddMenu;
import com.shawn.smartrestaurant.ui.main.commit.FragmentCommit;
import com.shawn.smartrestaurant.ui.main.dishes.FragmentDishes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
public class MainActivity extends AppCompatActivity {

    //
    private AppDatabase localDb;

    //
    FirebaseFirestore db;

    //
    private User user;

    //
    private NavHostFragment tablesNavHostFragment;

    //
    private NavHostFragment menuNavHostFragment;

    //
    private MenuItem currentMenuItem;

    //
    private Fragment currentFragment;

    //
    private DrawerLayout drawerLayout;

    //
    private NavigationView drawerNavigationView;

    //
    private ActionBarDrawerToggle actionBarDrawerToggle;

    //
    private Toolbar toolbar;

    //
    private AutoCompleteTextView autoCompleteTextView;

    //
    private StorageReference storageReference;

    //
    private List<Dish> dishList;

    //
//    private List<byte[]> dishImageList;

    //
    private Map<String, byte[]> menuImagesMap;

    //
    private Other other;


    /**
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set toolbar as action bar
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);

        // Set drawer layout with toolbar
        this.drawerLayout = findViewById(R.id.activity_main);

        //
        this.actionBarDrawerToggle = createActionBarDrawerToggle();

        //
        this.drawerLayout.addDrawerListener(this.actionBarDrawerToggle);

        // Set drawer navigation view
        this.drawerNavigationView = findViewById(R.id.nav_view);
        // TODO
        this.drawerNavigationView.getMenu().getItem(3).setActionView(R.layout.action_reminder_dot);
        setUpDrawerMenu(this.drawerNavigationView);

        if (null == this.currentMenuItem) {
            this.currentMenuItem = this.drawerNavigationView.getMenu().getItem(0);
            this.currentMenuItem.setChecked(true);
        }

        TextView drawerHeadUserId = this.drawerNavigationView.getHeaderView(0).findViewById(R.id.drawer_head_name);
        this.tablesNavHostFragment = NavHostFragment.create(R.navigation.nav_graph);
        this.menuNavHostFragment = NavHostFragment.create(R.navigation.nav_graph_menu_setting);

        this.localDb = AppDatabase.getInstance(getApplicationContext());
        List<User> users = this.localDb.userDao().findAll();
        if (null == users || 0 == users.size()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        } else {
            this.user = Objects.requireNonNull(users).get(0);
        }

        //
        drawerHeadUserId.setText(this.user.getId());

        // Set Fire Storage reference
        storageReference = FirebaseStorage.getInstance().getReference().child(ShawnOrder.COLLECTION_DISHES);

        // Get Fire Cloud instance
        db = FirebaseFirestore.getInstance();

        // Init dish list
        dishList = new ArrayList<>();
        menuImagesMap = new HashMap<>();

        //
        if (0 == getSupportFragmentManager().getFragments().size()) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentLayout_main, this.tablesNavHostFragment).commit();
//            getSupportFragmentManager().beginTransaction().add(R.id.fragmentLayout_main, this.menuNavHostFragment).commit();
        }
    }

    /**
     *
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.actionBarDrawerToggle.syncState();
    }

    /**
     *
     */
    public void setUpDrawerMenu(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(item -> {
                    if (this.currentMenuItem.getItemId() == item.getItemId()) {
                        this.drawerLayout.closeDrawers();
                        return true;
                    }
                    this.currentMenuItem = item;
                    item.setChecked(true);
                    switch (item.getItemId()) {
                        case R.id.fragment_drawer_tables:
//                            NavigationUI.setupWithNavController(toolbar, navController);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_main, this.tablesNavHostFragment).commit();
//                            getSupportFragmentManager().beginTransaction().add(R.id.fragmentLayout_main, FragmentTables.class, new Bundle()).commit();
//                            replaceFragment(new FragmentTables());
////                            currentFragmentTitle = "PROFILE";
                            break;
                        case R.id.fragment_drawer_menu:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_main, this.menuNavHostFragment).commit();
                            break;
                        case R.id.fragment_drawer_profile:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_main, FragmentProfile.class, new Bundle()).addToBackStack("FragmentTables").commit();
                            //replaceFragment(new FragmentProfile());
////                            currentFragmentTitle = "PROFILE";
                            break;
//                        case R.id.fragment_drawer_setting:
////                            drawerMenuIndex = 1;
////                            currentFragmentTitle = "SETTING";
//                            break;
                        default:
////                            drawerMenuIndex = 0;
                    }
                    this.drawerLayout.closeDrawers();
                    return true;
                }
        );
    }

    /**
     *
     */
//    public void replaceFragment(Fragment newFragment) {
////        Bundle args = new Bundle();
////        newFragment.setArguments(args);
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
////        transaction.add(newFragment,"TAG");
//        // Replace whatever is in the fragment_container view with this fragment,
//        // and add the transaction to the back stack so the user can navigate back
//        transaction.replace(R.id.fragmentLayout_main, newFragment);
//        transaction.addToBackStack(null);
//
//        // Commit the transaction
//        transaction.commit();
//        currentFragment = newFragment;
//    }

    /**
     *
     */
    public ActionBarDrawerToggle createActionBarDrawerToggle() {
        this.actionBarDrawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, this.toolbar, R.string.nav_app_bar_open_drawer_description, R.string.nav_app_bar_close_drawer_description);

        this.actionBarDrawerToggle.setToolbarNavigationClickListener(v -> {
            if (this.currentFragment instanceof FragmentDishes) {
                this.tablesNavHostFragment.getNavController().navigate(R.id.action_fragment_dishes_to_fragment_tables, new Bundle());
                this.actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            }

            if (this.currentFragment instanceof FragmentCommit) {
                this.tablesNavHostFragment.getNavController().navigate(R.id.action_fragment_commit_to_fragment_dishes, new Bundle());
            }

            if (this.currentFragment instanceof FragmentAddMenu) {
                this.menuNavHostFragment.getNavController().navigate(R.id.action_fragment_nav_addmenu_to_fragment_nav_menu, new Bundle());
                this.actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            }
        });

        return this.actionBarDrawerToggle;
    }

    /**
     *
     */
    public void clearFileDir() {
        if (null != Objects.requireNonNull(getFilesDir()).listFiles()) {
            for (File f : Objects.requireNonNull(getFilesDir().listFiles())) {
                if (f.getName().contains("jpg")) {
                    f.delete();
                }
            }
        }
    }

    /**
     *
     */
    public void downloadMenuImages(List<Dish> dishList) {

        if (null != dishList) {
            this.menuImagesMap = new HashMap<>();
            this.clearFileDir();

            for (Dish dish : Objects.requireNonNull(dishList)) {
                // TODO Add OnFailedListener
                storageReference.child(dish.getId() + ".jpg").getBytes(Code.TEN_MEGABYTE).addOnSuccessListener(bytes -> {
                    this.menuImagesMap.put(dish.getId(), bytes);
                });

                storageReference.child(dish.getId() + ".jpg").getFile(new File(getFilesDir(), dish.getId() + ".jpg")).addOnSuccessListener(taskSnapshot -> {
                });
            }
        }
    }

    /**
     *
     */
    public void refreshDishList(List<Dish> dishList) {

        if (null != dishList) {
            this.dishList = new ArrayList<>();
            localDb.dishDao().deleteAll();

            this.dishList = dishList;

            for (Dish dish : Objects.requireNonNull(dishList)) {
                localDb.dishDao().insert(dish);
            }
        }
    }


    /**
     *
     */
    public Fragment getCurrentFragment() {
        return this.currentFragment;
    }

    /**
     *
     */
    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    /**
     *
     */
    public MenuItem getCurrentMenuItem() {
        return this.currentMenuItem;
    }

    /**
     *
     */
    public void setCurrentMenuItem(MenuItem currentMenuItem) {
        this.currentMenuItem = currentMenuItem;
    }

    /**
     *
     */
    public DrawerLayout getDrawerLayout() {
        return this.drawerLayout;
    }

    /**
     *
     */
    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    /**
     *
     */
    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        return this.actionBarDrawerToggle;
    }

    /**
     *
     */
    public void setActionBarDrawerToggle(ActionBarDrawerToggle actionBarDrawerToggle) {
        this.actionBarDrawerToggle = actionBarDrawerToggle;
    }

    /**
     *
     */
    public NavHostFragment getTablesNavHostFragment() {
        return this.tablesNavHostFragment;
    }

    /**
     *
     */
    public void setTablesNavHostFragment(NavHostFragment tablesNavHostFragment) {
        this.tablesNavHostFragment = tablesNavHostFragment;
    }

    /**
     *
     */
    public AutoCompleteTextView getAutoCompleteTextView() {
        return this.autoCompleteTextView;
    }

    /**
     *
     */
    public void setAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView) {
        this.autoCompleteTextView = autoCompleteTextView;
    }

    /**
     *
     */
    public AppDatabase getLocalDb() {
        return localDb;
    }

    /**
     *
     */
    public void setLocalDb(AppDatabase localDb) {
        this.localDb = localDb;
    }

    /**
     *
     */
    public FirebaseFirestore getDb() {
        return db;
    }

    /**
     *
     */
    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }

    /**
     *
     */
    public User getUser() {
        return user;
    }

    /**
     *
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     */
    public StorageReference getStorageReference() {
        return storageReference;
    }

    /**
     *
     */
    public void setStorageReference(StorageReference storageReference) {
        this.storageReference = storageReference;
    }

    /**
     *
     */
    public NavHostFragment getMenuNavHostFragment() {
        return menuNavHostFragment;
    }

    /**
     *
     */
    public void setMenuNavHostFragment(NavHostFragment menuNavHostFragment) {
        this.menuNavHostFragment = menuNavHostFragment;
    }

    /**
     *
     */
    public List<Dish> getDishList() {
        return dishList;
    }

    /**
     *
     */
    public void setDishList(List<Dish> dishList) {
        this.dishList = dishList;
    }

//    /**
//     *
//     */
//    public List<byte[]> getDishImageList() {
//        return dishImageList;
//    }
//
//    /**
//     *
//     */
//    public void setDishImageList(List<byte[]> dishImageList) {
//        this.dishImageList = dishImageList;
//    }

    /**
     *
     */
    public Map<String, byte[]> getMenuImagesMap() {
        return menuImagesMap;
    }

    /**
     *
     */
    public void setMenuImagesMap(Map<String, byte[]> menuImagesMap) {
        this.menuImagesMap = menuImagesMap;
    }

    /**
     *
     */
    public Other getOther() {
        return other;
    }

    /**
     *
     */
    public void setOther(Other other) {
        this.other = other;
    }
}