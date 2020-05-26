package com.shawn.smartrestaurant.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.shawn.smartrestaurant.Code;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.AppDatabase;
import com.shawn.smartrestaurant.db.entity.Dish;
import com.shawn.smartrestaurant.db.entity.Other;
import com.shawn.smartrestaurant.db.entity.Table;
import com.shawn.smartrestaurant.db.entity.User;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;
import com.shawn.smartrestaurant.ui.login.LoginActivity;
import com.shawn.smartrestaurant.ui.main.addmenu.FragmentAddMenu;
import com.shawn.smartrestaurant.ui.main.done.FragmentOrderDone;
import com.shawn.smartrestaurant.ui.main.dishes.FragmentDishes;
import com.shawn.smartrestaurant.ui.main.history.done.FragmentHistoryOrderDone;
import com.shawn.smartrestaurant.ui.main.personnel.add.FragmentPersonnelAdd;
import com.shawn.smartrestaurant.ui.main.setting.FragmentSetting;

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
    private FirebaseFirestore db;

    //
    private User user;

    //
    private NavHostFragment tablesNavHostFragment;

    //
    private NavHostFragment menuNavHostFragment;

    //
    private NavHostFragment historyTablesNavHostFragment;

    //
    private NavHostFragment personnelNavHostFragment;

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
    private Map<String, Bitmap> menuImagesMap;

    //
    private Other other;

    //
    private Map<String, Table> tableMap;

    //
    private List<Table> historyTableList;

    //
    private boolean personnelChanged;

    //
    private List<User> memberList;


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
        // this.drawerNavigationView.getMenu().getItem(3).setActionView(R.layout.action_reminder_dot);
        setUpDrawerMenu(this.drawerNavigationView);

        if (null == this.currentMenuItem) {
            this.currentMenuItem = this.drawerNavigationView.getMenu().getItem(0);
            this.currentMenuItem.setChecked(true);
        }

        TextView drawerHeadUserId = this.drawerNavigationView.getHeaderView(0).findViewById(R.id.drawer_head_name);
        this.tablesNavHostFragment = NavHostFragment.create(R.navigation.nav_graph);
        this.menuNavHostFragment = NavHostFragment.create(R.navigation.nav_graph_menu_setting);
        this.historyTablesNavHostFragment = NavHostFragment.create(R.navigation.nav_graph_history);
        this.personnelNavHostFragment = NavHostFragment.create(R.navigation.nav_graph_personnel);

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

        // Get Tables information
//        db.collection(ShawnOrder.COLLECTION_TABLES).whereEqualTo(Table.COLUMN_GROUP, user.getGroup()).get().addOnSuccessListener(queryDocumentSnapshots -> {
//        });

        // Get extra information
        db.collection(ShawnOrder.COLLECTION_OTHERS).whereEqualTo(Other.COLUMN_GROUP, user.getGroup()).limit(1).get().addOnSuccessListener(queryDocumentSnapshots -> {
            this.other = queryDocumentSnapshots.getDocuments().get(0).toObject(Other.class);
        });

        // Init dish list
        dishList = new ArrayList<>();
        menuImagesMap = new HashMap<>();
        this.refreshMenu();

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
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_main, this.tablesNavHostFragment).commit();
                            break;
                        case R.id.fragment_drawer_menu:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_main, this.menuNavHostFragment).commit();
                            break;
                        case R.id.fragment_drawer_personnel:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_main, this.personnelNavHostFragment).commit();
                            break;
                        case R.id.fragment_drawer_setting:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_main, FragmentSetting.class, new Bundle()).commit();
                            break;
                        case R.id.fragment_drawer_history:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_main, this.historyTablesNavHostFragment).commit();
                            break;
                        default:
                            //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_main, this.tablesNavHostFragment).commit();
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

            if (this.currentFragment instanceof FragmentOrderDone) {
                Bundle bundle = new Bundle();
                bundle.putString(FragmentDishes.ARG_TABLE, new Gson().toJson(((FragmentOrderDone) currentFragment).getTable()));

                this.tablesNavHostFragment.getNavController().navigate(R.id.action_fragment_commit_to_fragment_dishes, bundle);
            }

            if (this.currentFragment instanceof FragmentPersonnelAdd) {
                this.personnelNavHostFragment.getNavController().navigate(R.id.action_framelayout_nav_personnel_add_to_framelayout_nav_personnel_members, new Bundle());
                this.actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            }

            if (this.currentFragment instanceof FragmentHistoryOrderDone) {
//                Bundle bundle = new Bundle();
//                bundle.putString(FragmentDishes.ARG_TABLE, new Gson().toJson(((FragmentOrderDone) currentFragment).getTable()));

                this.historyTablesNavHostFragment.getNavController().navigate(R.id.action_framelayout_nav_history_order_done_to_framelayout_nav_history_tables, new Bundle());
                this.actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
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
    public void refreshMenuImages() {
        this.menuImagesMap.clear();
//            this.clearFileDir();

        for (Dish dish : Objects.requireNonNull(this.dishList)) {
            if (dish.isHasImage()) {
                storageReference.child(dish.getId() + ".jpg").getBytes(Code.TEN_MEGABYTE).addOnSuccessListener(bytes -> {
                    this.menuImagesMap.put(dish.getId(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                }).addOnFailureListener(e -> {
                    // TODO Add OnFailedListener
                });
            }
        }
    }

    /**
     *
     */
//    public void refreshDishList(List<Dish> dishList) {
//
//        if (null != dishList) {
//            this.dishList = new ArrayList<>();
//            localDb.dishDao().deleteAll();
//
//            this.dishList = dishList;
//
//            for (Dish dish : Objects.requireNonNull(dishList)) {
//                localDb.dishDao().insert(dish);
//            }
//        }
//    }

    /**
     *
     */
//    public void getDbDishes(String group) {
//
//        //
//        db.collection(ShawnOrder.COLLECTION_OTHERS).whereEqualTo(Other.COLUMN_GROUP, group).get().addOnCompleteListener(task -> {
//            Other other = getOtherTask.getResult().getDocuments().get(0).toObject(Other.class);
//            if (((MainActivity) requireActivity()).getOther().getMenuVersion() != other.getMenuVersion()) {
//                ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_DISHES).whereEqualTo(Other.COLUMN_GROUP, ((MainActivity) requireActivity()).getUser().getGroup()).get().addOnCompleteListener(getDishesTask -> {
//                    List<Dish> dishList = new ArrayList<>();
//                    for () {
//                        getDishesTask.getResult().getDocuments().
//                    }
//                    getDishesTask.getResult().getDocuments().
//                });
//            }
//        }).addOnFailureListener(e -> {
//            // TODO
//        });
//    }

    /**
     *
     */
    public void refreshMenu() {
        db.collection(ShawnOrder.COLLECTION_DISHES).whereEqualTo(Dish.COLUMN_GROUP, user.getGroup()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            this.dishList.clear();
            for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                dishList.add(ds.toObject(Dish.class));
            }

            // refreshMenuImages
            refreshMenuImages();
        }).addOnFailureListener(e -> {
            // TODO
        });
    }

//    /**
//     *
//     */
//    public void checkVersionUp(Consumer consumer) {
//        consumer.accept();
//        db.collection(ShawnOrder.COLLECTION_OTHERS).whereEqualTo(Other.COLUMN_GROUP, user.getGroup()).get().addOnSuccessListener(queryDocumentSnapshots -> {
//            queryDocumentSnapshots.getDocuments().get(0).toObject(Other.class).getMenuVersion();
//        });
//        supplier
//db.collection()
//    }

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

    /**
     *
     */
    public Map<String, Bitmap> getMenuImagesMap() {
        return menuImagesMap;
    }

    /**
     *
     */
    public void setMenuImagesMap(Map<String, Bitmap> menuImagesMap) {
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

    /**
     *
     */
    public Map<String, Table> getTableMap() {
        return tableMap;
    }

    /**
     *
     */
    public void setTableMap(Map<String, Table> tableMap) {
        this.tableMap = tableMap;
    }

    /**
     *
     */
    public List<Table> getHistoryTableList() {
        return historyTableList;
    }

    /**
     *
     */
    public void setHistoryTableList(List<Table> historyTableList) {
        this.historyTableList = historyTableList;
    }

    /**
     *
     */
    public boolean isPersonnelChanged() {
        return personnelChanged;
    }

    /**
     *
     */
    public void setPersonnelChanged(boolean personnelChanged) {
        this.personnelChanged = personnelChanged;
    }

    /**
     *
     */
    public List<User> getMemberList() {
        return memberList;
    }

    /**
     *
     */
    public void setMemberList(List<User> memberList) {
        this.memberList = memberList;
    }
}