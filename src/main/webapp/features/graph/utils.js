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

import rough from 'roughjs/dist/rough.es5.umd';

/**
 * Get all attributes of an element as an array of { name, value } objects
 */

const getAttributes = (element) =>
  Array.prototype.slice.call(element.attributes);

/**
 * Return the numerical value of each attribute as an array of ints
 */

const getNum = (element, attributes) =>
  attributes.map((attribute) =>
    parseFloat(element.getAttribute(attribute), 10)
  );

/**
 * Return the numerical value of each attribute as an array of ints, multiplied by two
 */

const getDiam = (element, attributes) =>
  attributes.map(
    (attribute) => 2 * parseFloat(element.getAttribute(attribute), 10)
  );

/**
 * Convert a points attribute to an array that can be consumed by rough
 */

const getCoords = (element, attribute) =>
  element
    .getAttribute(attribute)
    .trim()
    .split(' ')
    .filter((item) => item.length > 0)
    .map((item) =>
      item
        .trim()
        .split(',')
        .map((num) => parseFloat(num, 10))
    );

/**
 * Converts attributes to settings for rough
 */

const getSettings = (element) => {
  const settings = {};

  if (element.hasAttribute('stroke')) {
    settings.stroke = element.getAttribute('stroke');
  }

  if (element.hasAttribute('fill')) {
    settings.fill = element.getAttribute('fill');
  }

  if (
    element.hasAttribute('stroke-width') &&
    !element.getAttribute('stroke-width').includes('%')
  ) {
    settings.strokeWidth = parseFloat(element.getAttribute('stroke-width'), 10);
  }

  return settings;
};

const blacklist = [
  'cx',
  'cy',
  'd',
  'fill',
  'height',
  'points',
  'r',
  'rx',
  'ry',
  'stroke-width',
  'stroke',
  'width',
  'x',
  'x1',
  'x2',
  'y',
  'y1',
  'y2'
];

export const coarse = (originalSvg, options = { roughness: 1 }) => {
  const svg = originalSvg.cloneNode(true);
  const rc = rough.svg(svg, { options });

  // Get all descendants of the svg that should be processed
  const children = svg.querySelectorAll(
    'circle, rect, ellipse, line, polygon, polyline, path'
  );

  // Loop through all child elements
  for (let i = 0; i < children.length; i += 1) {
    const original = children[i];
    const params = [];
    let shapeType;

    try {
      switch (original.tagName) {
        case 'circle':
          params.push(
            ...getNum(original, ['cx', 'cy']),
            ...getDiam(original, ['r'])
          );
          shapeType = 'circle';
          break;
        case 'rect':
          params.push(...getNum(original, ['x', 'y', 'width', 'height']));
          shapeType = 'rectangle';
          break;
        case 'ellipse':
          params.push(
            ...getNum(original, ['cx', 'cy']),
            ...getDiam(original, ['rx', 'ry'])
          );
          shapeType = 'ellipse';
          break;
        case 'line':
          params.push(...getNum(original, ['x1', 'y1', 'x2', 'y2']));
          shapeType = 'line';
          break;
        case 'polygon':
          params.push(getCoords(original, 'points'));
          shapeType = 'polygon';
          break;
        case 'polyline':
          params.push(getCoords(original, 'points'));
          shapeType = 'linearPath';
          break;
        case 'path':
          params.push(original.getAttribute('d'));
          shapeType = 'path';
          break;
      }

      // Generate the new shape
      const replacement = rc[shapeType](...params, getSettings(original));

      // Get all attributes from the original element that should be copied over
      const attributes = getAttributes(original).filter(
        (attribute) => !blacklist.includes(attribute.name)
      );

      // Copy all valid attributes to the replacement
      attributes.forEach(({ name, value }) => {
        replacement.setAttribute(name, value);
      });

      original.replaceWith(replacement);
    } catch (err) {
      console.log(err);
    }
  }

  // We remove the first stroke pattern because it's a useless
  // white stroke covering the whole graph. I don't know why this
  // pattern exists and I'd rather implement this dirty fix than
  // look it up right now.

  const indexOfStyleToDelete = svg.outerHTML.indexOf('style="stroke: white"');
  const indexOfPathStart = svg.outerHTML.indexOf(
    '<path',
    indexOfStyleToDelete - 500 // rofl
  );
  const indexOfPathEnd = svg.outerHTML.indexOf('</path>', indexOfPathStart);
  const before = svg.outerHTML.substring(0, indexOfPathStart);
  const after = svg.outerHTML.substring(indexOfPathEnd, svg.outerHTML.length);
  return before + after;
};
