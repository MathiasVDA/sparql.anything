package io.github.sparqlanything.engine;

import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.main.QC;

import java.util.List;
import java.util.Properties;

import static io.github.sparqlanything.engine.PropertyExtractor.extractPropertiesFromBGP;

public class FXWorkerOpBGP extends FXWorker<OpBGP> {


	public FXWorkerOpBGP(TriplifierRegister tr, DatasetGraphCreator dgc) {
		super(tr, dgc);
	}


	@Override
	public QueryIterator execute(OpBGP op, QueryIterator input, ExecutionContext executionContext, DatasetGraph dg, Properties p) {
		ExecutionContext newExecContext = Utils.getNewExecutionContext(executionContext, p, dg);

		List<Triple> magicPropertyTriples = Utils.getFacadeXMagicPropertyTriples(op.getPattern());
		if (!magicPropertyTriples.isEmpty()) {
			return QC.execute(Utils.excludeMagicPropertyTriples(Utils.excludeFXProperties(op)), executeMagicProperties(input, magicPropertyTriples, newExecContext), newExecContext);
		} else {
			return QC.execute(Utils.excludeFXProperties(op), input, newExecContext);
		}
	}

	@Override
	public void extractProperties(Properties p, OpBGP op) throws UnboundVariableException {
		extractPropertiesFromBGP(p, op);
	}

	private QueryIterator executeMagicProperties(QueryIterator input, List<Triple> propFuncTriples, ExecutionContext execCxt) {
		QueryIterator input2 = input;
		for (Triple t : propFuncTriples) {
			input2 = QC.execute(Utils.getOpPropFuncAnySlot(t), input2, execCxt);
		}
		return input2;
	}
}