package com.HIRFA.HIRFA.Statistiques;

import com.HIRFA.HIRFA.repository.ClientRepository;
import com.HIRFA.HIRFA.repository.DesignerRepository;
import com.HIRFA.HIRFA.repository.CooperativeRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatistiquesService {

    private final ClientRepository clientRepo;
    private final DesignerRepository designerRepo;
    private final CooperativeRepository cooperativeRepo;

    public StatistiquesService(ClientRepository clientRepo,
            DesignerRepository designerRepo,
            CooperativeRepository cooperativeRepo) {
        this.clientRepo = clientRepo;
        this.designerRepo = designerRepo;
        this.cooperativeRepo = cooperativeRepo;
    }

    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("clients", clientRepo.count());
        stats.put("designers", designerRepo.count());
        stats.put("cooperatives", cooperativeRepo.count());
        return stats;
    }
}
