import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/files">
        <Translate contentKey="global.menu.entities.files" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/tests">
        <Translate contentKey="global.menu.entities.tests" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/questions">
        <Translate contentKey="global.menu.entities.questions" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-test">
        <Translate contentKey="global.menu.entities.userTest" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/my-users">
        <Translate contentKey="global.menu.entities.myUsers" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
