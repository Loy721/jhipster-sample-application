import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/my-file">
        <Translate contentKey="global.menu.entities.myFile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/my-test">
        <Translate contentKey="global.menu.entities.myTest" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/my-question">
        <Translate contentKey="global.menu.entities.myQuestion" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
