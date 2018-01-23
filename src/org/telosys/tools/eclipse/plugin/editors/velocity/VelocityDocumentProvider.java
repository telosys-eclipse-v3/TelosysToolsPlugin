package org.telosys.tools.eclipse.plugin.editors.velocity;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.telosys.tools.eclipse.plugin.editors.velocity.scanner.partition.VelocityPartitionScanner;

public class VelocityDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner =
//				new FastPartitioner(
//					new VelocityPartitionScanner(),
//					new String[] {
//						VelocityPartitionScanner.XML_TAG,
//						VelocityPartitionScanner.VELOCITY_MULTILINE_COMMENT });
				new FastPartitioner( new VelocityPartitionScanner(), VelocityPartitionScanner.TYPES );
			
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}