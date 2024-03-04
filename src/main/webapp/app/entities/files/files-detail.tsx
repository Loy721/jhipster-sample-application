import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './files.reducer';

export const FilesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const filesEntity = useAppSelector(state => state.files.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="filesDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.files.detail.title">Files</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="jhipsterSampleApplicationApp.files.id">Id</Translate>
            </span>
          </dt>
          <dd>{filesEntity.id}</dd>
          <dt>
            <span id="topic">
              <Translate contentKey="jhipsterSampleApplicationApp.files.topic">Topic</Translate>
            </span>
          </dt>
          <dd>{filesEntity.topic}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="jhipsterSampleApplicationApp.files.content">Content</Translate>
            </span>
          </dt>
          <dd>{filesEntity.content}</dd>
        </dl>
        <Button tag={Link} to="/files" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/files/${filesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FilesDetail;
