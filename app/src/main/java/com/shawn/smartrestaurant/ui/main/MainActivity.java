package com.shawn.smartrestaurant.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private ActionBarDrawerToggle actionBarDrawerToggle;

    //
    private Toolbar toolbar;

    //
    private AutoCompleteTextView autoCompleteTextView;

    //
//    private StorageReference storageReference;

    //
    private List<Dish> dishList;

//    //
//    private Map<String, Bitmap> menuImagesMap;

    //
    private Other other;

    //
    private Map<String, Table> tableMap;

    //
    private List<User> memberList;

    //
    private List<Table> historyTableList;

//    //
//    private boolean personnelChanged;

    //
    private boolean menuChanged;


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
        NavigationView drawerNavigationView = findViewById(R.id.nav_view);

        // Set drawer header Logout button
        drawerNavigationView.getHeaderView(0).findViewById(R.id.button_drawer_header_logout).setOnClickListener(v -> this.logout());

        // TODO
        // this.drawerNavigationView.getMenu().getItem(3).setActionView(R.layout.action_reminder_dot);
        setUpDrawerMenu(drawerNavigationView);

        if (null == this.currentMenuItem) {
            this.currentMenuItem = drawerNavigationView.getMenu().getItem(0);
            this.currentMenuItem.setChecked(true);
        }


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
        ((TextView) drawerNavigationView.getHeaderView(0).findViewById(R.id.drawer_head_name)).setText(this.user.getId());


        // Set Fire Storage reference
//        this.storageReference = FirebaseStorage.getInstance().getReference().child(ShawnOrder.COLLECTION_DISHES);

        // Get Fire Cloud instance
        this.db = FirebaseFirestore.getInstance();

        // Get extra information
//        this.db.collection(ShawnOrder.COLLECTION_OTHERS).whereEqualTo(Other.COLUMN_GROUP, user.getGroup()).limit(1).get().addOnSuccessListener(queryDocumentSnapshots -> {
//            this.other = queryDocumentSnapshots.getDocuments().get(0).toObject(Other.class);
//        });
        // Init dish list
//        this.dishList = new ArrayList<>();
//        this.menuImagesMap = new HashMap<>();
//        this.refreshMenu();

        //
        if (0 == getSupportFragmentManager().getFragments().size()) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentLayout_main, this.tablesNavHostFragment).commit();
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
//                        default:
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_main, this.tablesNavHostFragment).commit();
                    }
                    this.drawerLayout.closeDrawers();
                    return true;
                }
        );
    }

//    /**
//     *
//     */
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
                this.historyTablesNavHostFragment.getNavController().navigate(R.id.action_framelayout_nav_history_order_done_to_framelayout_nav_history_tables, new Bundle());
                this.actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            }

            if (this.currentFragment instanceof FragmentAddMenu) {
                this.menuNavHostFragment.getNavController().navigate(R.id.action_framelayout_nav_addmenu_to_framelayout_nav_menu, new Bundle());
                this.actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            }
        });

        return this.actionBarDrawerToggle;
    }

//    /**
//     *
//     */
//    public void clearFileDir() {
//        if (null != Objects.requireNonNull(getFilesDir()).listFiles()) {
//            for (File f : Objects.requireNonNull(getFilesDir().listFiles())) {
//                if (f.getName().contains("jpg")) {
//                    f.delete();
//                }
//            }
//        }
//    }

//    /**
//     *
//     */
//    public void refreshMenuImages() {
//        this.menuImagesMap.clear();
////            this.clearFileDir();
//
//        for (Dish dish : Objects.requireNonNull(this.dishList)) {
//            if (dish.isHasImage()) {
//                storageReference.child(dish.getId() + ".jpg").getBytes(Code.TEN_MEGABYTE).addOnSuccessListener(bytes -> {
//                    this.menuImagesMap.put(dish.getId(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
//                }).addOnFailureListener(e -> {
//                    // TODO Add OnFailedListener
//                });
//            }
//        }
//    }

    /**
     *
     */
    public void logout() {
        this.localDb.userDao().delete(this.user);

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     *
     */
    public void authenticate() {
        this.db.collection(ShawnOrder.COLLECTION_USERS).document(this.user.getId()).get().addOnSuccessListener(documentSnapshot -> {
            debug(Code.LOG_DB_DEBUG_TAG, "Get user in authenticate method of MainActivity.");

            User latest = documentSnapshot.toObject(User.class);
            if (!latest.getId().equals(this.user.getId()) || latest.getUpdateTime() != this.user.getUpdateTime()) {
                this.logout();
            }
        }).addOnFailureListener(e -> this.logout());
    }

    /**
     *
     */
    public void dataUpdate(TextView listenerDishes, TextView listenerTables, TextView listenerUsers, TextView listenerHistory) {
        // Block UI and show progress bar
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//        processBar.setVisibility(View.VISIBLE);
        if (null == this.other) {
            this.other = new Other();
            this.other.setId(this.user.getGroup());
        }

        this.db.collection(ShawnOrder.COLLECTION_OTHERS).document(this.user.getGroup()).get().addOnSuccessListener(documentSnapshot -> {
            debug(Code.LOG_DB_DEBUG_TAG, "Get other in dataUpdate method of MainActivity.");

            Other latestOther = documentSnapshot.toObject(Other.class);
            if (this.other.getMenuVersion() != Objects.requireNonNull(latestOther).getMenuVersion()) {
                this.updateDishes(listenerDishes, latestOther);
            } else {
                if (null != listenerDishes) {
                    listenerDishes.setText(Code.READY);
                }
            }

            if (this.other.getTableVersion() != Objects.requireNonNull(latestOther).getTableVersion()) {
                this.updateTables(listenerTables, latestOther);
            } else {
                if (null != listenerTables) {
                    listenerTables.setText(Code.READY);
                }
            }

            if (this.other.getMemberVersion() != Objects.requireNonNull(latestOther).getMemberVersion()) {
                this.updateUsers(listenerUsers, latestOther);
            } else {
                if (null != listenerUsers) {
                    listenerUsers.setText(Code.READY);
                }
            }

            if (this.other.getHistoryVersion() != Objects.requireNonNull(latestOther).getHistoryVersion()) {
                this.updateHistory(listenerHistory, latestOther);
            } else {
                if (null != listenerHistory) {
                    listenerHistory.setText(Code.READY);
                }
            }

            // Release blocking UI and hide progress bar
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            processBar.setVisibility(View.GONE);
        });
    }

    /**
     *
     */
    public void updateDishes(TextView listener, Other latestOther) {

        this.dishList = new ArrayList<>();
        this.db.collection(ShawnOrder.COLLECTION_DISHES).whereEqualTo(Dish.COLUMN_GROUP, this.user.getGroup()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            debug(Code.LOG_DB_DEBUG_TAG, "Get dishes in updateDishes method of MainActivity.");

            for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                this.dishList.add(ds.toObject(Dish.class));
            }

            this.other.setMenuVersion(latestOther.getMenuVersion());

            if (null != listener) {
                listener.setText(Code.READY);
            }

//            Collections.sort(this.dishList, (o1, o2) -> {
//                if (null == o1.getDishCode() && null == o2.getDishCode()) {
//                    return 0;
//                } else if (null == o1.getDishCode()) {
//                    return "".compareTo(o2.getDishCode());
//                } else if (null == o2.getDishCode()) {
//                    return o1.getDishCode().compareTo("");
//                } else {
//                    return o1.getDishCode().compareTo(o2.getDishCode());
//                }
//            });
        });
    }

    /**
     *
     */
    public void updateTables(TextView listener, Other latestOther) {

        this.tableMap = new HashMap<>();
        this.db.collection(ShawnOrder.COLLECTION_TABLES).whereEqualTo(Table.COLUMN_GROUP, this.user.getGroup()).orderBy(Table.COLUMN_ID).get().addOnSuccessListener(qdsTables -> {
            debug(Code.LOG_DB_DEBUG_TAG, "Get tables in updateTables method of MainActivity.");

            for (DocumentSnapshot ds : qdsTables.getDocuments()) {
                Table table = ds.toObject(Table.class);
//                table.setDishList(this.dishList);
//                table.setUpdateTime(System.currentTimeMillis());
//                table.setUpdateUser(this.user.getId());
                this.tableMap.put(Objects.requireNonNull(table).getId(), table);
            }

            this.other.setTableVersion(latestOther.getTableVersion());

            if (null != listener) {
                listener.setText(Code.READY);
            }
        });
    }

    /**
     *
     */
    public void updateUsers(TextView listener, Other latestOther) {
        this.memberList = new ArrayList<>();
        this.db.collection(ShawnOrder.COLLECTION_USERS).whereEqualTo(User.COLUMN_GROUP, this.user.getGroup()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            debug(Code.LOG_DB_DEBUG_TAG, "Get user in updateUsers method of MainActivity.");

            for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                this.memberList.add(ds.toObject(User.class));
            }

            this.other.setMemberVersion(latestOther.getMemberVersion());

            if (null != listener) {
                listener.setText(Code.READY);
            }
        });
    }

    /**
     *
     */
    public void updateHistory(TextView listener, Other latestOther) {
        this.historyTableList = new ArrayList<>();
        this.db.collection(ShawnOrder.COLLECTION_HISTORY).whereEqualTo(Table.COLUMN_GROUP, this.user.getGroup()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            debug(Code.LOG_DB_DEBUG_TAG, "Get history in updateHistory method of MainActivity.");

            for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                this.historyTableList.add(ds.toObject(Table.class));
            }

            this.other.setHistoryVersion(latestOther.getHistoryVersion());

            if (null != listener) {
                listener.setText(Code.READY);
            }
        });
    }

//    /**
//     *
//     */
//    public void updateOthers() {
//        this.db.collection(ShawnOrder.COLLECTION_OTHERS).document(this.user.getGroup()).get().addOnSuccessListener(documentSnapshot -> {
//            debug(Code.LOG_DB_DEBUG_TAG, "Get other in updateOthers method.");
//
//            this.other = documentSnapshot.toObject(Other.class);
//        });
//    }

    /**
     *
     */
    public static void debug(String tag, String message) {
        if (Code.IS_DEBUG_MODE) {
            Log.d(tag, message);
        }
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

//    /**
//     *
//     */
//    public boolean isPersonnelChanged() {
//        return personnelChanged;
//    }
//
//    /**
//     *
//     */
//    public void setPersonnelChanged(boolean personnelChanged) {
//        this.personnelChanged = personnelChanged;
//    }

    /**
     *
     */
    public boolean isMenuChanged() {
        return menuChanged;
    }

    /**
     *
     */
    public void setMenuChanged(boolean menuChanged) {
        this.menuChanged = menuChanged;
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