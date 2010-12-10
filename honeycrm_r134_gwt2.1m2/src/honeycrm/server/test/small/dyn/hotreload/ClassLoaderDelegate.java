/*
 * Copyright 2010 @ashigeru.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package honeycrm.server.test.small.dyn.hotreload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;

public abstract class ClassLoaderDelegate {
    protected byte[] findClass(String binaryName) {
        InputStream stream = findResourceAsStream(InterceptClassLoader.toClassFilePath(binaryName));
        if (stream == null) {
            return null;
        }
        try {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1000];
            while (true) {
                int read = stream.read(buffer);
                if (read < 0) {
                    break;
                }
                result.write(buffer, 0, read);
            }
            return result.toByteArray();
        }
        catch (IOException e) {
            return null;
        }
        finally {
            try {
                stream.close();
            }
            catch (IOException e) {
                // ignored.
            }
        }
    }

    protected URL findResource(String path) {
        Iterator<URL> found = findAllResources(path).iterator();
        if (found.hasNext()) {
            return found.next();
        }
        return null;
    }

    protected Iterable<URL> findAllResources(String path) {
        return Collections.emptyList();
    }

    protected InputStream findResourceAsStream(String path) {
        URL location = findResource(path);
        if (location == null) {
            return null;
        }
        try {
            return location.openStream();
        }
        catch (IOException e) {
            return null;
        }
    }
}