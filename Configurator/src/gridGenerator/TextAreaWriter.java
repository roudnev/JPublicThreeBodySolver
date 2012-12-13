/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gridGenerator;
import java.io.Writer;
import java.io.IOException;
import javax.swing.JTextArea;
/**
 * Simple way to "print" to a JTextArea; just say
 * PrintWriter out = new PrintWriter(new TextAreaWriter(myTextArea));
 * Then out.println() et all will all appear in the TextArea.
 */
public final class TextAreaWriter extends Writer {

	private final JTextArea textArea;

	public TextAreaWriter(final JTextArea textArea) {
		this.textArea = textArea;
	}

    @Override
    public void flush(){ }

    @Override
    public void close(){ }

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		textArea.append(new String(cbuf, off, len));
	}
}
