package com.example.debarshibanerjee.projectfiredemon.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.debarshibanerjee.projectfiredemon.data.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by debarshibanerjee on 29/09/17.
 */
@Table(database = MyDatabase.class)
public class Contributor extends BaseModel implements Parcelable {
    @PrimaryKey
    @Column String login;
    @Column String html_url;
    @Column int contributions;

    public String getLogin() {
        return login;
    }

    public String getHtmlUrl() {
        return html_url;
    }

    public int getContributions() {
        return contributions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.login);
        dest.writeString(this.html_url);
        dest.writeInt(this.contributions);
    }

    public Contributor() {
    }

    protected Contributor(Parcel in) {
        this.login = in.readString();
        this.html_url = in.readString();
        this.contributions = in.readInt();
    }

    public static final Parcelable.Creator<Contributor> CREATOR = new Parcelable.Creator<Contributor>() {
        @Override
        public Contributor createFromParcel(Parcel source) {
            return new Contributor(source);
        }

        @Override
        public Contributor[] newArray(int size) {
            return new Contributor[size];
        }
    };
}
