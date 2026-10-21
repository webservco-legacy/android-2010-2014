package co.webserv.android.apocket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.TextView;

public class AboutActivity extends Activity {

    private GeneralAdapter mGeneralAdapter;

    private TextView mContent;

    private TextView mDev;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mGeneralAdapter = new GeneralAdapter(this);
        if (!mGeneralAdapter.onCreate()) {
            return;
        }

        setContentView(R.layout.main);

        mContent = (TextView) findViewById(R.id.content);

        mContent.setText(Html.fromHtml(getString(R.string.content_about)));
        mContent.setMovementMethod(LinkMovementMethod.getInstance());

        mDev = (TextView) findViewById(R.id.dev);

        mDev.setVisibility(View.VISIBLE);

        registerForContextMenu(mDev);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mGeneralAdapter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return mGeneralAdapter.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mGeneralAdapter.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        menu.setHeaderIcon(R.drawable.icon);
        menu.setHeaderTitle(R.string.dev_context);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dev_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
        case R.id.dev_db_import:
            Intent idi = new Intent(this, DatabaseImportActivity.class);
            idi.putExtra(GeneralAdapter.msKeyUserId, mGeneralAdapter.mUserId);
            startActivityForResult(idi, GeneralAdapter.RESULT_CLOSE_ALL);
            mGeneralAdapter.exit();
            return true;
        case R.id.dev_db_backup:
            Intent idb = new Intent(this, DatabaseBackupActivity.class);
            idb.putExtra(GeneralAdapter.msKeyUserId, mGeneralAdapter.mUserId);
            startActivityForResult(idb, GeneralAdapter.RESULT_CLOSE_ALL);
            mGeneralAdapter.exit();
            return true;
        case R.id.dev_db_restore:
            Intent idr = new Intent(this, DatabaseRestoreActivity.class);
            idr.putExtra(GeneralAdapter.msKeyUserId, mGeneralAdapter.mUserId);
            startActivityForResult(idr, GeneralAdapter.RESULT_CLOSE_ALL);
            mGeneralAdapter.exit();
            return true;
        case R.id.dev_db_delete:
            Intent idd = new Intent(this, DatabaseDeleteActivity.class);
            idd.putExtra(GeneralAdapter.msKeyUserId, mGeneralAdapter.mUserId);
            startActivityForResult(idd, GeneralAdapter.RESULT_CLOSE_ALL);
            mGeneralAdapter.exit();
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        mGeneralAdapter.onBackPressed();
    }
}
/* $Id$ */