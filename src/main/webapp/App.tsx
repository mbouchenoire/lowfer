/*
 * Copyright 2020 the original author or authors from the Lowfer project.
 *
 * This file is part of the Lowfer project, see https://github.com/mbouchenoire/lowfer
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React, { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import {
  BrowserRouter as Router,
  Redirect,
  Route,
  Switch
} from 'react-router-dom';

import { actions } from './features/architectures/slice';
import routes, { RoutePath } from './routes';

import './App.scss';
import Header from './components/Header';

const App = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(actions.getList());
  }, [dispatch]);

  return (
    <Router>
      <>
        <div className="App">
          <header className="Header">
            <Header />
          </header>
          <div className="Content">
            <div className="Detail">
              <Switch>
                {routes.map(({ path, Component }) => (
                  <Route key={path} path={path}>
                    {Component}
                  </Route>
                ))}
                <Redirect to={RoutePath.DRAFT} />
              </Switch>
            </div>
          </div>
        </div>
      </>
    </Router>
  );
};

export default App;
