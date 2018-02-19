/*
 * Copyright (c) 2018, www.libmonk.com
 * All rights reserved.
 *
 * BSD 3-Clause License
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.photos.UserFeed;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;

public class PhotosReader {

    private static DataStoreFactory dataStoreFactory;

    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    public static void main(String[] args) throws Exception {
        // Using File as data store ensures we retain the authentication info until it expires.
        dataStoreFactory = new FileDataStoreFactory(new File("src/main/resources/datastore"));
        Credential credential = authorize();
        PicasawebService service = new PicasawebService("myapp");
        service.setOAuth2Credentials(credential);

        // Observe the term "default" in the URL, this means the logged in user, for other users, the user id needs to be used.
        // Refer official documentation for more details.
        URL feedUrl = new URL("https://picasaweb.google.com/data/feed/api/user/default?kind=album");
        UserFeed feed = service.getFeed(feedUrl, UserFeed.class);
        System.out.println("Albums");
        feed.getAlbumEntries().forEach(entry -> System.out.println(entry.getTitle().getPlainText()));
    }

    private static Credential authorize() throws Exception {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(PhotosReader.class.getResourceAsStream("/client_secret.json")));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
                Collections.singleton("https://www.googleapis.com/auth/photos")).setDataStoreFactory(dataStoreFactory)
                .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }


}
