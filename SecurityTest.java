import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.rsbot.Configuration;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;

@ScriptManifest(authors = { "Paris" }, keywords = { "Development", "Security" }, name = "Security Test", version = 1.0, description = "Stress tests the bots security.")
public class SecurityTest extends Script {

	public static void download(final URL url, final File file)
			throws IOException {
		final URLConnection con = url.openConnection();

		if (file.exists()) {
			file.delete();
		}

		final DataInputStream di = new DataInputStream(con.getInputStream());
		final byte[] buffer = new byte[con.getContentLength()];
		di.readFully(buffer);
		di.close();

		final FileOutputStream fos = new FileOutputStream(file);
		fos.write(buffer);
		fos.flush();
		fos.close();
	}

	@Override
	public int loop() {
		stopScript(false);
		return 0;
	}

	@Override
	public boolean onStart() {
		final File dir = getCacheDirectory();
		File file;
		String test;
		final String anchor = "--test--";
		final File fileAds = new File(Configuration.Paths.getCacheDirectory()
				+ File.separator + "ads.txt");

		test = "script cache";
		try {
			if (!dir.exists()) {
				throw new IOException();
			}
			file = new File(dir, "test.txt");
			write(file, anchor);
			final String s = read(file);
			if (!s.equals(anchor)) {
				throw new IOException();
			}
			log.info("Test passed: " + test);
		} catch (final SecurityException ignored) {
			log.warning("Test failed: " + test);
		} catch (final IOException ignored) {
			log.warning("Test error: " + test);
		}

		test = "disallowed path (read)";
		file = fileAds;
		try {
			read(file);
			log.warning("Test failed: " + test);
		} catch (final SecurityException ignored) {
			log.info("Test passed: " + test);
		} catch (final IOException ignored) {
			log.warning("Test error: " + test);
		}

		test = "disallowed path (delete)";
		try {
			file.delete();
			log.warning("Test failed: " + test);
		} catch (final SecurityException ignored) {
			log.info("Test passed: " + test);
		}

		test = "disallowed path (write)";
		try {
			write(file, anchor);
			log.warning("Test failed: " + test);
		} catch (final SecurityException ignored) {
			log.info("Test passed: " + test);
		} catch (final IOException ignored) {
			log.warning("Test error: " + test);
		}

		test = "accounts file";
		file = new File(Configuration.Paths.getAccountsFile());
		try {
			read(file);
			log.warning("Test failed: " + test);
		} catch (final SecurityException ignored) {
			log.info("Test passed: " + test);
		} catch (final IOException ignored) {
			log.warning("Test error: " + test);
		}

		test = "downloading whitelisted";
		file = new File(dir, "image.png");
		try {
			SecurityTest.download(new URL("http://i.imgur.com/9lmr9.png"), file);
			log.info("Test passed: " + test);
		} catch (final SecurityException ignored) {
			log.warning("Test failed: " + test);
			ignored.printStackTrace();
		} catch (final IOException ignored) {
			log.warning("Test error: " + test);
		}

		test = "downloading blacklisted";
		try {
			SecurityTest.download(new URL("http://www.example.com/"), file);
			log.warning("Test failed: " + test);
		} catch (final SecurityException ignored) {
			log.info("Test passed: " + test);
		} catch (final IOException ignored) {
			log.warning("Test error: " + test);
		}

		test = "override SecurityManager";
		try {
			System.setSecurityManager(new SecurityManager());
			log.warning("Test failed: " + test);
		} catch (final SecurityException ignored) {
			log.info("Test passed: " + test);
		}

		threadInvoke("new thread (accounts)", new File(Configuration.Paths.getAccountsFile()));
		threadInvoke("new thread (generic)", fileAds);

		log.info("Test complete");
		return false;
	}

	private String read(final File file) throws IOException {
		final StringBuilder sb = new StringBuilder((int) file.length());
		final FileReader fstream = new FileReader(file);
		final BufferedReader in = new BufferedReader(fstream);
		int r;
		while ((r = in.read()) != -1) {
			sb.append((char) r);
		}
		return sb.toString();
	}

	private void threadInvoke(final String test, final File file) {
		try {
			final Thread thread = new Thread("hidden") {
				@Override
				public void run() {
					try {
						read(file);
						log.warning("Test failed: " + test);
					} catch (final SecurityException ignored) {
						log.info("Test passed: " + test);
					} catch (final IOException ignored) {
						log.warning("Test error: " + test);
					}
				}
			};
			thread.start();
			thread.join();
		} catch (final InterruptedException ignored) {
			log.warning("Test error: " + test);
		}
	}

	private void write(final File file, final String text) throws IOException {
		final FileWriter fstream = new FileWriter(file);
		final BufferedWriter out = new BufferedWriter(fstream);
		out.write(text);
		out.close();
		fstream.close();
	}

}
