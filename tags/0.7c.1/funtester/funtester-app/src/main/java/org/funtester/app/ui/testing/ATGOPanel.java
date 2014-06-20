package org.funtester.app.ui.testing;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import org.funtester.app.ui.util.BaseTableModel;
import org.funtester.core.process.atgo.ATGO;
import org.funtester.core.process.atgo.UseCaseOption;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Abstract Test Generation Option Panel
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ATGOPanel extends JPanel {

	private static final long serialVersionUID = 2453609015031645611L;
	private JTable useCaseOptionsTable;
	private final ATGO atgo = new ATGO();
	private JTextField testSuiteName;

	public ATGOPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("378px:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("253px:grow"),}));
		
		JLabel lblTestSuiteName = new JLabel("Test Suite Name:");
		add(lblTestSuiteName, "1, 1, right, default");
		
		testSuiteName = new JTextField();
		testSuiteName.setName("testSuiteName");
		testSuiteName.setText("testSuiteName");
		add(testSuiteName, "3, 1, fill, default");
		testSuiteName.setColumns(10);
		
		JScrollPane useCaseOptionsScrollPane = new JScrollPane();
		useCaseOptionsScrollPane.setName("useCaseOptionsScrollPane");
		add(useCaseOptionsScrollPane, "1, 3, 3, 1, fill, fill");
		
		useCaseOptionsTable = new JTable( createTableModel() );
		useCaseOptionsTable.setName("useCaseOptionsTable");
		useCaseOptionsScrollPane.setViewportView( useCaseOptionsTable );

	}
	
	private TableModel createTableModel() {
		
		final String columns[] = {
				"#",
				"a",
				"b"
		}; // i18n
		
		BaseTableModel< UseCaseOption > model = new BaseTableModel< UseCaseOption >(
				columns.length,
				atgo.getUseCaseOptions(),
				columns
				) {
				private static final long serialVersionUID = 8095857805329341413L;

				@Override
				public Object getValueAt(int rowIndex, int columnIndex) {
					switch ( columnIndex ) {
						case 0: return rowIndex + 1;
						default: return "";
					}
				}
		};
		
		return model;
	}
}
