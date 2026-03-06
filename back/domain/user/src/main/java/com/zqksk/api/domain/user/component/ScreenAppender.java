package com.zqksk.api.domain.user.component;

import com.zqksk.api.domain.user.model.request.NewScreen;
import com.zqksk.api.domain.user.model.response.Screen;
import com.zqksk.api.domain.user.repository.ScreenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScreenAppender {

    private final ScreenRepository screenRepository;

    public Screen save(NewScreen screen){
        return screenRepository.save(screen);
    }



}
