package com.teamobi.avatar2.service.impl;

import com.teamobi.avatar2.network.ISession;
import com.teamobi.avatar2.service.IFarmService;

public class FarmService implements IFarmService {
    private final ISession session;

    public FarmService(ISession session) {
        this.session = session;
    }
}
