package de.dogedev.ld35.ashley;

import com.badlogic.ashley.core.ComponentMapper;
import de.dogedev.ld35.ashley.components.*;

/**
 * Created by Furuha on 12.03.2016.
 */
public class ComponentMappers {

    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<VelocityComponent> velocity = ComponentMapper.getFor(VelocityComponent.class);
    public static final ComponentMapper<SpriteComponent> sprite = ComponentMapper.getFor(SpriteComponent.class);
    public static final ComponentMapper<AnimationComponent> animation = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<NameComponent> name = ComponentMapper.getFor(NameComponent.class);

}
