package org.ai.dao.interfaces.analise;

import java.util.List;

import org.ai.bo.analise.CasoTeste;

public interface CasoDAO {

    public int createCaso(CasoTeste caso);

    public boolean deleteCaso(CasoTeste caso);

    public CasoTeste findCaso(int idCaso);

    public List<CasoTeste> listCaso();
}
